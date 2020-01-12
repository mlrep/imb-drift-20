import math
import random
import utils.arff as arff

root_dir = 'streams'


def create_drifting_stream(streams, drift_defs, s_size):
    print('Creating drifting stream')
    drifting_stream = []
    d_idx = 0
    drift = drift_defs[d_idx]
    print(drift)

    for i in range(0, s_size):
        prob = sigm(drift['p'], drift['w'], i)
        r = random.uniform(0, 1)

        if prob > r:
            new_sample = streams[drift['c'][0]][i]
        else:
            new_sample = streams[drift['c'][1]][i]

        drifting_stream.append(new_sample)

        if (i > drift['p'] + drift['w'] / 2.0) and d_idx < len(drift_defs) - 1:
            d_idx += 1
            drift = drift_defs[d_idx]
            print(drift)

    return drifting_stream


def sigm(p, w, x):
    try:
        ans = 1 - (1.0 / (1 + math.exp((-4.0 / w) * (x - p))))
    except OverflowError:
        if x < p:
            ans = 1
        else:
            ans = 0
    return ans


def create_real_drifting_stream(base_stream, drift_defs, mappings, s_size):
    print('Creating drifting stream')
    drifting_stream = []
    d_idx = 0
    drift = drift_defs[d_idx]
    c_idx = 0
    concepts = drift_defs[c_idx]['c']
    dc_num = 0
    first_pw = drift_defs[0]['p'] - (drift_defs[0]['w'] / 2)

    print(drift)
    print(concepts)

    for i in range(0, s_size):
        prob = sigm(drift['p'], drift['w'], i)
        r = random.uniform(0, 1)
        base_row = base_stream[i]
        #print(i, base_row)

        if prob > r:
            new_sample_cls = mappings[drift['c'][0]][base_row[-1]]
        else:
            new_sample_cls = mappings[drift['c'][1]][base_row[-1]]

        if new_sample_cls != mappings[concepts[0]][base_row[-1]] and i >= first_pw:
            dc_num += 1

        base_row[-1] = new_sample_cls
        drifting_stream.append(base_row)

        if (i > drift['p'] + drift['w'] / 2.0) and d_idx < len(drift_defs) - 1:
            d_idx += 1
            drift = drift_defs[d_idx]
            print(drift)

        if d_idx >= 1 and i == drift['p'] - drift['w'] / 2.0:
            concepts = drift_defs[d_idx]['c']
            print(i, concepts)

    print(dc_num, s_size - first_pw)
    dc_ratio = dc_num / (s_size - first_pw)
    print(dc_ratio)

    return drifting_stream


def create_real_drifting_streams():
    arff_data = arff.load_arff('real/ACTIVITY', root_dir, False)
    ds = create_real_drifting_stream(arff_data['data'], [
        {'p': 5000, 'w': 100, 'c': ['m1', 'm2']}], {
            'm1': {'Walking': '1', 'Jogging': '1', 'Stairs': '2', 'Upstairs': '2', 'Downstairs': '2', 'Sitting': '4', 'Standing': '3', 'LyingDown': '4'},
            'm2': {'Walking': '3', 'Jogging': '2', 'Stairs': '1', 'Upstairs': '1', 'Downstairs': '1', 'Sitting': '3', 'Standing': '4', 'LyingDown': '4'}
    }, 10853)
    arff.write_arff(replace_classes(arff_data['attributes'], ['1', '2', '3', '4']), ds, 'imbalanced/dynamic/semi-synth/ACTIVITY-D1', root_dir)

    arff_data = arff.load_arff('real/ACTIVITY_RAW', root_dir, False)
    ds = create_real_drifting_stream(arff_data['data'], [
        {'p': 500000, 'w': 10000, 'c': ['m1', 'm2']}], {
            'm1': {'Walking': '1', 'Jogging': '1', 'Upstairs': '2', 'Downstairs': '2', 'Sitting': '3', 'Standing': '4'},
            'm2': {'Walking': '4', 'Jogging': '4', 'Upstairs': '3', 'Downstairs': '3', 'Sitting': '1', 'Standing': '2'}
    }, 1048570)
    arff.write_arff(replace_classes(arff_data['attributes'], ['1', '2', '3', '4']), ds, 'imbalanced/dynamic/semi-synth/ACTIVITY_RAW-D1', root_dir)

    arff_data = arff.load_arff('real/CONNECT4', root_dir, False)
    ds = create_real_drifting_stream(arff_data['data'], [
        {'p': 20000, 'w': 100, 'c': ['m1', 'm2']}, {'p': 50000, 'w': 100, 'c': ['m2', 'm3']}], {
            'm1': {'win': '1', 'loss': 2, 'draw': 3},
            'm2': {'win': '3', 'loss': 2, 'draw': 1},
            'm3': {'win': '2', 'loss': 1, 'draw': 3}
    }, 67557)
    arff.write_arff(replace_classes(arff_data['attributes'], ['1', '2', '3']), ds, 'imbalanced/dynamic/semi-synth/CONNECT4-D1', root_dir)

    arff_data = arff.load_arff('real/COVERTYPE', root_dir, False)
    ds = create_real_drifting_stream(arff_data['data'], [
        {'p': 250000, 'w': 10000, 'c': ['m1', 'm2']}], {
            'm1': {'1': '1', '2': '2', '3': '3', '4': '3', '5': '3', '6': '4', '7': '4'},
            'm2': {'1': '4', '2': '4', '3': '2', '4': '3', '5': '3', '6': '1', '7': '1'}
    }, 581012)
    arff.write_arff(replace_classes(arff_data['attributes'], ['1', '2', '3', '4']), ds, 'imbalanced/dynamic/semi-synth/COVERTYPE-D1', root_dir)

    arff_data = arff.load_arff('real/DJ30', root_dir, False)
    ds = create_real_drifting_stream(arff_data['data'], [
        {'p': 40000, 'w': 1000, 'c': ['m1', 'm2']}, {'p': 80000, 'w': 1000, 'c': ['m2', 'm3']}], {
            'm1': {'aa': '1', 'axp': '1', 'bs': '1', 'cat': '1', 'ci': '1', 'co': '1', 'dd': '1', 'dis': '1',
                   'ek': '1', 'ge': '1', 'gm': '1', 'hd': '1', 'hon': '1', 'hpq': '1', 'ibm': '1', 'intc': '1',
                   'ip': '1', 'jnj': '1', 'jpm': '1', 'mcd': '1', 'mmm': '2', 'mo': '2', 'mrk': '2', 'msft': '2',
                   'pg': '2', 'sbc': '2', 't': '3', 'utx': '3', 'wmt': '4', 'xom': '4'},
            'm2': {'aa': '4', 'axp': '4', 'bs': '4', 'cat': '4', 'ci': '4', 'co': '4', 'dd': '4', 'dis': '4',
                   'ek': '4', 'ge': '4', 'gm': '4', 'hd': '4', 'hon': '4', 'hpq': '4', 'ibm': '4', 'intc': '4',
                   'ip': '4', 'jnj': '4', 'jpm': '4', 'mcd': '4', 'mmm': '3', 'mo': '3', 'mrk': '3', 'msft': '3',
                   'pg': '3', 'sbc': '3', 't': '2', 'utx': '2', 'wmt': '1', 'xom': '1'},
            'm3': {'aa': '2', 'axp': '2', 'bs': '2', 'cat': '2', 'ci': '2', 'co': '2', 'dd': '2', 'dis': '2',
                   'ek': '2', 'ge': '2', 'gm': '2', 'hd': '2', 'hon': '2', 'hpq': '2', 'ibm': '2', 'intc': '2',
                   'ip': '2', 'jnj': '2', 'jpm': '2', 'mcd': '2', 'mmm': '1', 'mo': '1', 'mrk': '1', 'msft': '1',
                   'pg': '1', 'sbc': '1', 't': '4', 'utx': '4', 'wmt': '3', 'xom': '3'},
    }, 138166)
    arff.write_arff(replace_classes(arff_data['attributes'], ['1', '2', '3', '4']), ds, 'imbalanced/dynamic/semi-synth/DJ30-D1', root_dir)

    arff_data = arff.load_arff('real/GAS', root_dir, False)
    ds = create_real_drifting_stream(arff_data['data'], [
        {'p': 7000, 'w': 100, 'c': ['m1', 'm2']}], {
            'm1': {'1': '1', '2': '1', '3': '1', '4': '2', '5': '1', '6': '3'},
            'm2': {'1': '3', '2': '1', '3': '2', '4': '3', '5': '3', '6': '3'}
    }, 13910)
    arff.write_arff(replace_classes(arff_data['attributes'], ['1', '2', '3']), ds, 'imbalanced/dynamic/semi-synth/GAS-D1', root_dir)

    arff_data = arff.load_arff('real/SENSOR', root_dir, False)
    ds = create_real_drifting_stream(arff_data['data'], [
        {'p': 700000, 'w': 10000, 'c': ['m1', 'm2']}, {'p': 1400000, 'w': 10000, 'c': ['m2', 'm3']}], {
            'm1': {'1': '1', '2': '1', '3': '1', '4': '1', '5': '1', '6': '1', '7': '1', '8': '1', '9': '1', '10': '1',
                   '11': '1', '12': '1', '13': '1', '14': '1', '15': '1', '16': '1', '17': '1', '18': '1', '19': '1', '20': '1',
                   '21': '1', '22': '1', '23': '1', '24': '1', '25': '1', '26': '1', '27': '1', '28': '1', '29': '1', '30': '1',
                   '31': '1', '32': '1', '33': '1', '34': '1', '35': '1', '36': '1', '37': '1', '38': '1', '39': '1', '40': '1',
                   '41': '2', '42': '2', '43': '2', '44': '2', '45': '2', '46': '2', '47': '2', '48': '2', '49': '2', '50': '2',
                   '51': '3', '52': '3', '53': '3', '54': '3', '55': '4', '56': '4', '57': '4', '58': '4'},
            'm2': {'1': '4', '2': '4', '3': '4', '4': '4', '5': '4', '6': '4', '7': '4', '8': '4', '9': '4', '10': '4',
                   '11': '4', '12': '4', '13': '4', '14': '4', '15': '4', '16': '4', '17': '4', '18': '4', '19': '4', '20': '4',
                   '21': '4', '22': '4', '23': '4', '24': '4', '25': '4', '26': '4', '27': '4', '28': '4', '29': '4', '30': '4',
                   '31': '4', '32': '4', '33': '4', '34': '4', '35': '4', '36': '4', '37': '4', '38': '4', '39': '4', '40': '4',
                   '41': '3', '42': '3', '43': '3', '44': '3', '45': '3', '46': '3', '47': '3', '48': '3', '49': '3', '50': '3',
                   '51': '2', '52': '2', '53': '2', '54': '2', '55': '1', '56': '1', '57': '1', '58': '1'},
            'm3': {'1': '2', '2': '2', '3': '2', '4': '2', '5': '2', '6': '2', '7': '2', '8': '2', '9': '2', '10': '2',
                   '11': '2', '12': '2', '13': '2', '14': '2', '15': '2', '16': '2', '17': '2', '18': '2', '19': '2', '20': '2',
                   '21': '2', '22': '2', '23': '2', '24': '2', '25': '2', '26': '2', '27': '2', '28': '2', '29': '2', '30': '2',
                   '31': '2', '32': '2', '33': '2', '34': '2', '35': '2', '36': '2', '37': '2', '38': '2', '39': '2', '40': '2',
                   '41': '1', '42': '1', '43': '1', '44': '1', '45': '1', '46': '1', '47': '1', '48': '1', '49': '1', '50': '1',
                   '51': '4', '52': '4', '53': '4', '54': '4', '55': '3', '56': '3', '57': '3', '58': '3'},
    }, 2219802)
    arff.write_arff(replace_classes(arff_data['attributes'], ['1', '2', '3', '4']), ds, 'imbalanced/dynamic/semi-synth/SENSOR-D1', root_dir)

    arff_data = arff.load_arff('real/POKER', root_dir, False)
    ds = create_real_drifting_stream(arff_data['data'], [
        {'p': 300000, 'w': 10000, 'c': ['m1', 'm2']}, {'p': 600000, 'w': 10000, 'c': ['m2', 'm3']}], {
            'm1': {'0': '1', '1': '1', '2': '2', '3': '2', '4': '3', '5': '3', '6': '4', '7': '4', '8': '3', '9': '4'},
            'm2': {'0': '4', '1': '4', '2': '3', '3': '3', '4': '2', '5': '2', '6': '1', '7': '1', '8': '2', '9': '1'},
            'm3': {'0': '2', '1': '2', '2': '1', '3': '1', '4': '4', '5': '4', '6': '3', '7': '3', '8': '4', '9': '3'}
    }, 829201)
    arff.write_arff(replace_classes(arff_data['attributes'], ['1', '2', '3', '4']), ds, 'imbalanced/dynamic/semi-synth/POKER-D1', root_dir)

    arff_data = arff.load_arff('real/OLYMPIC', root_dir, False)
    ds = create_real_drifting_stream(arff_data['data'], [
        {'p': 90000, 'w': 1000, 'c': ['m1', 'm2']}, {'p': 180000, 'w': 1000, 'c': ['m2', 'm3']}], {
            'm1': {'None': '1', 'Bronze': '2', 'Silver': '2', 'Gold': '3'},
            'm2': {'None': '2', 'Bronze': '1', 'Silver': '3', 'Gold': '2'},
            'm3': {'None': '3', 'Bronze': '1', 'Silver': '1', 'Gold': '2'}
    }, 271116)
    arff.write_arff(replace_classes(arff_data['attributes'], ['1', '2', '3']), ds, 'imbalanced/dynamic/semi-synth/OLYMPIC-D1', root_dir)

    arff_data = arff.load_arff('real/TAGS', root_dir, False)
    ds = create_real_drifting_stream(arff_data['data'], [
        {'p': 50000, 'w': 1000, 'c': ['m1', 'm2']}, {'p': 100000, 'w': 1000, 'c': ['m2', 'm3']}], {
            'm1': {'walking': '1', 'falling': '4', 'lying_down': '3', 'lying': '1', 'sitting_down': '4', 'sitting': '1',
                   'standing_up_from_lying': '2', 'on_all_fours': '3', 'sitting_on_the_ground': '2', 'standing_up_from_sitting': '4',
                   'standing_up_from_sitting_on_the_ground': '4'},
            'm2': {'walking': '4', 'falling': '1', 'lying_down': '2', 'lying': '4', 'sitting_down': '1', 'sitting': '4',
                   'standing_up_from_lying': '3', 'on_all_fours': '2', 'sitting_on_the_ground': '3', 'standing_up_from_sitting': '1',
                   'standing_up_from_sitting_on_the_ground': '1'},
            'm3': {'walking': '2', 'falling': '1', 'lying_down': '4', 'lying': '2', 'sitting_down': '3', 'sitting': '2',
                   'standing_up_from_lying': '1', 'on_all_fours': '4', 'sitting_on_the_ground': '1', 'standing_up_from_sitting': '3',
                   'standing_up_from_sitting_on_the_ground': '3'}
    }, 164860)
    arff.write_arff(replace_classes(arff_data['attributes'], ['1', '2', '3', '4']), ds, 'imbalanced/dynamic/semi-synth/TAGS-D1', root_dir)

    arff_data = arff.load_arff('real/CRIMES', root_dir, False)
    ds = create_real_drifting_stream(arff_data['data'], [
        {'p': 300000, 'w': 10000, 'c': ['m1', 'm2']}, {'p': 600000, 'w': 10000, 'c': ['m2', 'm3']}], {
            'm1': {'WARRANTS': '2', 'OTHER_OFFENSES': '1', 'LARCENY': '1', 'VEHICLE_THEFT': '2', 'VANDALISM': '2',
                   'NON-CRIMINAL': '1', 'ROBBERY': '3', 'ASSAULT': '1', 'WEAPON_LAWS': '4', 'BURGLARY': '3',
                   'SUSPICIOUS_OCC': '3', 'DRUNKENNESS': '4', 'FORGERY': '4', 'DRUG': '2', 'STOLEN_PROPERTY': '4',
                   'SECONDARY_CODES': '4', 'TRESPASS': '4', 'MISSING_PERSON': '3', 'FRAUD': '4', 'KIDNAPPING': '4',
                   'RUNAWAY': '4', 'DRIVING_UNDER_INFLUENCE': '4', 'SEX_OFFENSES_FORCIBLE': '4', 'PROSTITUTION': '4',
                   'DISORDERLY_CONDUCT': '4', 'ARSON': '4', 'FAMILY_OFFENSES': '4', 'LIQUOR_LAWS': '4', 'BRIBERY': '4',
                   'EMBEZZLEMENT': '4', 'SUICIDE': '4', 'LOITERING': '4', 'SEX_OFFENSES_NON_FORCIBLE': '4', 'EXTORTION': '4',
                   'GAMBLING': '4', 'BAD_CHECKS': '4', 'TREA': '4', 'RECOVERED_VEHICLE': '4', 'PORNOGRAPHY': '4'},
            'm2': {'WARRANTS': '4', 'OTHER_OFFENSES': '3', 'LARCENY': '3', 'VEHICLE_THEFT': '4', 'VANDALISM': '4',
                   'NON-CRIMINAL': '3', 'ROBBERY': '1', 'ASSAULT': '3', 'WEAPON_LAWS': '2', 'BURGLARY': '1',
                   'SUSPICIOUS_OCC': '1', 'DRUNKENNESS': '2', 'FORGERY': '2', 'DRUG': '4', 'STOLEN_PROPERTY': '2',
                   'SECONDARY_CODES': '2', 'TRESPASS': '2', 'MISSING_PERSON': '1', 'FRAUD': '2', 'KIDNAPPING': '2',
                   'RUNAWAY': '2', 'DRIVING_UNDER_INFLUENCE': '2', 'SEX_OFFENSES_FORCIBLE': '2', 'PROSTITUTION': '2',
                   'DISORDERLY_CONDUCT': '2', 'ARSON': '2', 'FAMILY_OFFENSES': '2', 'LIQUOR_LAWS': '2', 'BRIBERY': '2',
                   'EMBEZZLEMENT': '2', 'SUICIDE': '2', 'LOITERING': '2', 'SEX_OFFENSES_NON_FORCIBLE': '2', 'EXTORTION': '2',
                   'GAMBLING': '2', 'BAD_CHECKS': '2', 'TREA': '2', 'RECOVERED_VEHICLE': '2', 'PORNOGRAPHY': '2'},
            'm3': {'WARRANTS': '1', 'OTHER_OFFENSES': '2', 'LARCENY': '2', 'VEHICLE_THEFT': '1', 'VANDALISM': '1',
                   'NON-CRIMINAL': '2', 'ROBBERY': '4', 'ASSAULT': '2', 'WEAPON_LAWS': '3', 'BURGLARY': '4',
                   'SUSPICIOUS_OCC': '4', 'DRUNKENNESS': '3', 'FORGERY': '3', 'DRUG': '1', 'STOLEN_PROPERTY': '3',
                   'SECONDARY_CODES': '3', 'TRESPASS': '3', 'MISSING_PERSON': '4', 'FRAUD': '3', 'KIDNAPPING': '3',
                   'RUNAWAY': '3', 'DRIVING_UNDER_INFLUENCE': '3', 'SEX_OFFENSES_FORCIBLE': '3', 'PROSTITUTION': '3',
                   'DISORDERLY_CONDUCT': '3', 'ARSON': '3', 'FAMILY_OFFENSES': '3', 'LIQUOR_LAWS': '3', 'BRIBERY': '3',
                   'EMBEZZLEMENT': '3', 'SUICIDE': '3', 'LOITERING': '3', 'SEX_OFFENSES_NON_FORCIBLE': '3', 'EXTORTION': '3',
                   'GAMBLING': '3', 'BAD_CHECKS': '3', 'TREA': '3', 'RECOVERED_VEHICLE': '3', 'PORNOGRAPHY': '3'}
    }, 878049)
    arff.write_arff(replace_classes(arff_data['attributes'], ['1', '2', '3', '4']), ds, 'imbalanced/dynamic/semi-synth/CRIMES-D1', root_dir)

    arff_data = arff.load_arff('real/ELEC', root_dir, False)
    ds = create_real_drifting_stream(arff_data['data'], [
        {'p': 20000, 'w': 500, 'c': ['m1', 'm2']}], {
                                         'm1': {'UP': '1', 'DOWN': '2'},
                                         'm2': {'UP': '2', 'DOWN': '1'}
                                     }, 45312)
    arff.write_arff(replace_classes(arff_data['attributes'], ['1', '2']), ds, 'imbalanced/dynamic/semi-synth/ELEC-D1', root_dir)


def replace_classes(atts, new_classes):
    atts[-1] = ('class', new_classes)
    return atts


def main():
    print("Running...")
    create_real_drifting_streams()


if __name__ == "__main__":
    main()
