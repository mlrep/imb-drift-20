import arff


def load_arff(fp, root_dir, m=True):
    print('Loading {0}'.format(fp))
    name = fp.split('/')[-1]
    arff_data = arff.load(open('{0}/{1}.arff'.format(root_dir, fp), 'r'))

    if m:
        return {name: arff_data}
    else:
        return arff_data


def split_similar_arffs_fields(arffs):
    data = {}
    for key, value in arffs.items():
        data[key] = value['data']

    return [
        data,
        arffs[list(arffs.keys())[0]]['attributes']
    ]


def write_arff(header, data, fp, root_dir):
    print('Writing {0}'.format(fp))
    new_arff = {'attributes': header, 'data': data, 'relation': fp, 'description': ''}

    with open('{0}/{1}.arff'.format(root_dir, fp), "w") as fh:
        arff.dump(new_arff, fh)
