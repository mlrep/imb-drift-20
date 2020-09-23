from joblib import Parallel, delayed
import multiprocessing
import utils.arff as arff
from utils.window import WindowedValue
import vis.lin as lin

root_dir = 'D:/Computer_Science/Projects/Data/streams'


def generate_ratio_for_streams(arffs, wf):
    num_cores = multiprocessing.cpu_count()
    Parallel(n_jobs=num_cores)(
        delayed(generate_ratio_for_stream)(a, wf, '{0}/{1}'.format(root_dir, name)) for name, a in arffs.items()
    )


def generate_ratio_for_stream(arff_data, wf, fp):
    print('Generating ratios for {0}'.format(fp))
    [classes, stream] = [arff_data['attributes'][-1][1], arff_data['data']]
    ratios = calc_ratios(stream, classes, wf, fp)
    vis_ratio(ratios, fp)


def calc_ratios(stream, classes, wf, fp):
    win_values = {}
    ratios = {}
    win_size = wf * len(stream)

    for c in classes:
        win_values[c] = WindowedValue(win_size)
        ratios[c] = []

    log = int(0.01 * len(stream))

    for i in range(0, len(stream) - 1):
        #if i % log == 0:
            #print(i)

        row = stream[i]
        cls = row[-1]

        for c in classes:
            if c != cls:
                win_values[c].add(0)
            else:
                win_values[c].add(1)

        if i > win_size:
            for c in classes:
                ratios[c].append(win_values[c].get_avg())

    # for c in classes:
    #     print(fp, c, ratios[c][-1])

    return ratios


def vis_ratio(ratios, fp):
    lin.plot_lin(ratios, fp)


def generate_ratios_for_real():
    arffs = arff.load_arffs(['real/POKER/POKER', 'real/ELEC/ELEC', 'real/GAS/GAS', 'real/ACTIVITY/RAW/ACTIVITY_RAW', 'real/ACTIVITY/TRANSFORMED/ACTIVITY',
                             'real/CONNECT4/CONNECT4', 'real/COVERTYPE/COVERTYPE', 'real/DJ30/DJ30', 'real/KDD99/KDD99',
                             'real/SPAM//SPAM09/SPAM', 'real/USENET/USENET', 'real/WEATHER/WEATHER', 'real/AIRLINES/AIRLINES',
                             'real/OLYMPIC/OLYMPIC', 'real/CRIMES/CRIMES', 'real/TAGS/TAGS', 'real/EEG/EEG', 'imbalanced/static/binary/CENSUS/CENSUS'], root_dir)
    generate_ratio_for_streams(arffs, 0.05)

    fp = 'real/SENSOR/SENSOR'
    a = arff.load_arff(fp, root_dir)
    generate_ratio_for_stream(a[list(a.keys())[0]], 0.05, '{0}/{1}'.format(root_dir, fp.split('/')[-1]))


def generate_ratios_for_synth():
    arffs = arff.load_arffs(
        ['imbalanced/dynamic/SEA_R_W100', 'imbalanced/dynamic/SEA_RC_W100', 'imbalanced/dynamic/SEA_R_W100k', 'imbalanced/dynamic/SEA_RC_W100k',
         'imbalanced/dynamic/SEA_RB_W20k', 'imbalanced/dynamic/SEA_RBC_W20k', 'imbalanced/dynamic/SEA_RS_W10k', 'imbalanced/dynamic/SEA_RSC_W10k',
         'imbalanced/dynamic/STAGGER_R_W100', 'imbalanced/dynamic/STAGGER_RC_W100', 'imbalanced/dynamic/STAGGER_R_W100k', 'imbalanced/dynamic/STAGGER_RC_W100k',
         'imbalanced/dynamic/STAGGER_RB_W20k', 'imbalanced/dynamic/STAGGER_RBC_W20k', 'imbalanced/dynamic/STAGGER_RS_W10k', 'imbalanced/dynamic/STAGGER_RSC_W10k',
         'imbalanced/dynamic/SINE_R_W100', 'imbalanced/dynamic/SINE_RC_W100', 'imbalanced/dynamic/SINE_R_W100k', 'imbalanced/dynamic/SINE_RC_W100k',
         'imbalanced/dynamic/SINE_RB_W20k', 'imbalanced/dynamic/SINE_RBC_W20k', 'imbalanced/dynamic/SINE_RS_W10k', 'imbalanced/dynamic/SINE_RSC_W10k',
         'imbalanced/dynamic/RBF_R_W100', 'imbalanced/dynamic/RBF_RC_W100', 'imbalanced/dynamic/RBF_R_W100k', 'imbalanced/dynamic/RBF_RC_W100k',
         'imbalanced/dynamic/RBF_RB_W20k', 'imbalanced/dynamic/RBF_RBC_W20k', 'imbalanced/dynamic/RBF_RS_W10k', 'imbalanced/dynamic/RBF_RSC_W10k',
         'imbalanced/dynamic/TREE_R_W100', 'imbalanced/dynamic/TREE_RC_W100', 'imbalanced/dynamic/TREE_R_W100k', 'imbalanced/dynamic/TREE_RC_W100k',
         'imbalanced/dynamic/TREE_RB_W20k', 'imbalanced/dynamic/TREE_RBC_W20k', 'imbalanced/dynamic/TREE_RS_W10k', 'imbalanced/dynamic/TREE_RSC_W10k'
         ],
        root_dir)
    generate_ratio_for_streams(arffs, 0.05)


def generate_ratios_for_dynamic_semi_synth():
    arffs = arff.load_arffs(['real/SENSOR/SENSOR'], root_dir)
    generate_ratio_for_streams(arffs, 0.05)


def generate_ratios_for_static_semi_synth():
    arffs = arff.load_arffs(['imbalanced/static/semi-synth/ACTIVITY_RAW-S1', 'imbalanced/static/semi-synth/DJ30-S1', 'imbalanced/static/semi-synth/SENSOR-S1',
                             'imbalanced/static/semi-synth/OLYMPIC-S1', 'imbalanced/static/semi-synth/CRIMES-S1', 'imbalanced/static/semi-synth/TAGS-S1',
                             'imbalanced/static/semi-synth/POKER-S1'], root_dir)
    generate_ratio_for_streams(arffs, 0.05)


def main():
    print("Running...")
    #generte_ratios_for_real()
    #generate_ratios_for_synth()
    generate_ratios_for_dynamic_semi_synth()
    #generate_ratios_for_static_semi_synth()


if __name__ == "__main__":
    main()
