from plots import pgfplot

labels_map = {
    'ENS#CLUST-STAT': 'SCL',
    'ENS#CLUST': 'CL',
    'ENS#CLUST+SE': 'CL+FX',
    'ENS#CLUST+SERR': 'CL+ER',
    'ENS#CLUST-DIV': 'CLD',
    'ENS#CLUST-STAB': 'CLS',
    'ENS#CLUST-DIV+SE': 'CLD+FX',
    'ENS#CLUST-DIV+SERR': 'CLD+ER',
    'ENS#CLUST-STAB+SE': 'CLS+FX',
    'ENS#CLUST-STAB+SERR': 'CLS+ER',
    'ENS#BAG': 'BAG',
    'ENS#BAG-DIV': 'BAG-D',
    'ENS#BAG-STAB': 'BAG-S',
    'ENS#CLUST-L1': 'CL-1',
    'ENS#CLUST-L2': 'CL-2',
    'ENS#CLUST-L3': 'CL-3',
    'ENS#CLUST-L4': 'CL-4'
}


def generate_ctrl():
    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\ctrl\\ctrl_SEA1_acc.dat',
        size=600000, g=100, ag=[35, 35, 35], init=60000, p=[150000, 300000, 450000], w=100, omit=5000, lb_path=None,
        labels_map=labels_map, output_name='ctrl/ctrl_SEA1_acc', sel_series=['CLD', 'CLS', 'BAG-S'],
        legend=True, xticks=False, ylabel='Accuracy', title='SEA1')
    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\ctrl\\ctrl_SEA1_D.dat',
        size=600000, g=100, ag=[30, 30, 30], init=60000, p=[150000, 300000, 450000], w=100, omit=5000, lb_path=None,
        labels_map=labels_map, output_name='ctrl/ctrl_SEA1_D', sel_series=['CLD', 'CLS', 'BAG-S'],
        legend=False, xticks=True, ylabel='D', title=None)

    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\ctrl\\ctrl_STAGGER2_kappa.dat',
        size=600000, g=100, ag=[10, 10, 10], init=60000, p=[150000, 300000, 450000], w=10000, omit=1000, lb_path=None,
        labels_map=labels_map, output_name='ctrl/ctrl_STAGGER2_kappa', sel_series=['CLD', 'CLS', 'BAG-D'],
        legend=True, xticks=False, ylabel='Kappa', title='STAGGER2')
    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\ctrl\\ctrl_STAGGER2_D.dat',
        size=600000, g=100, ag=[10, 10, 10], init=60000, p=[150000, 300000, 450000], w=10000, omit=1000, lb_path=None,
        labels_map=labels_map, output_name='ctrl/ctrl_STAGGER2_D', sel_series=['CLD', 'CLS', 'BAG-D'],
        legend=False, xticks=True, ylabel='D', title=None, yticks=[0.0, 0.15, 0.30, 0.45], ylim=[-0.05, 0.50])

    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\ctrl\\ctrl_TREE3_acc.dat',
        size=1200000, g=100, ag=[20, 20, 20], init=120000, p=[400000, 800000], w=50000, omit=1000, lb_path=None,
        labels_map=labels_map, output_name='ctrl/ctrl_TREE3_acc', sel_series=['CLD', 'CLS', 'BAG-D'],
        legend=True, xticks=False, ylabel='Accuracy', title='TREE3', yticks=[0.20, 0.40, 0.60, 0.80, 1.00], ylim=[0.15, 1.05])
    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\ctrl\\ctrl_TREE3_D.dat',
        size=1200000, g=100, ag=[20, 20, 20], init=120000, p=[400000, 800000], w=50000, omit=1000, lb_path=None,
        labels_map=labels_map, output_name='ctrl/ctrl_TREE3_D', sel_series=['CLD', 'CLS', 'BAG-D'],
        legend=False, xticks=True, ylabel='D', title=None)

    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\ctrl\\ctrl_RBF4_kappa.dat',
        size=1200000, g=100, ag=[35, 35, 35], init=120000, p=[400000, 800000], w=100000, omit=60000, lb_path=None,
        labels_map=labels_map, output_name='ctrl/ctrl_RBF4_kappa', sel_series=['CLD', 'CLS', 'BAG-S'],
        legend=True, xticks=False, ylabel='Kappa', title='RBF4', yticks=[0.80, 0.85, 0.90, 0.95, 1.00], ylim=[0.79, 1.02])
    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\ctrl\\ctrl_RBF4_D.dat',
        size=1200000, g=100, ag=[30, 30, 30], init=120000, p=[400000, 800000], w=100000, omit=60000, lb_path=None,
        labels_map=labels_map, output_name='ctrl/ctrl_RBF4_D', sel_series=['CLD', 'CLS', 'BAG-S'],
        legend=False, xticks=True, ylabel='D', title=None, yticks=[0.0, 0.10, 0.20, 0.30, 0.40], ylim=[-0.02, 0.42])


def generate_enh():
    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\enh\\enh_TREE1_acc.dat',
        size=1000000, g=100, ag=[25, 35, 25], init=100000, p=[250000, 500000, 750000], w=100, omit=1000, lb_path=None,
        labels_map=labels_map, output_name='enh/enh_TREE1_acc', sel_series=['CL', 'CL+FX', 'CL+ER'],
        legend=True, xticks=False, ylabel='Accuracy', title='TREE1', ylim=[0.10, 1.05])
    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\enh\\enh_TREE1_D.dat',
        size=1000000, g=100, ag=[10, 35, 10], init=100000, p=[250000, 500000, 750000], w=100, omit=1000, lb_path=None,
        labels_map=labels_map, output_name='enh/enh_TREE1_D', sel_series=['CL', 'CL+FX', 'CL+ER'],
        legend=False, xticks=True, ylabel='D', title=None, ylim=[-0.02, 0.42])

    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\enh\\enh_RBF2_kappa.dat',
        size=1000000, g=100, ag=[25, 35, 25], init=100000, p=[250000, 500000, 750000], w=10000, omit=1000, lb_path=None,
        labels_map=labels_map, output_name='enh/enh_RBF2_kappa', sel_series=['CL', 'CL+FX', 'CL+ER'],
        legend=True, xticks=False, ylabel='Kappa', title='RBF2')
    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\enh\\enh_RBF2_D.dat',
        size=1000000, g=100, ag=[15, 20, 15], init=100000, p=[250000, 500000, 750000], w=10000, omit=1000, lb_path=None,
        labels_map=labels_map, output_name='enh/enh_RBF2_D', sel_series=['CL', 'CL+FX', 'CL+ER'],
        legend=False, xticks=True, ylabel='D', title=None, ylim=[-0.02, 0.27])

    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\enh\\enh_HYPERPLANE1_acc.dat',
        size=500000, g=100, ag=[20, 20, 20], init=50000, p=[], w=0, omit=1000, lb_path=None,
        labels_map=labels_map, output_name='enh/enh_HYPERPLANE1_acc', sel_series=['CL', 'CL+FX', 'CL+ER'],
        legend=True, xticks=False, ylabel='Accuracy', title='HYPER1', yticks=[0.65, 0.75, 0.85, 0.95], ylim=[0.62, 0.97])
    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\enh\\enh_HYPERPLANE1_D.dat',
        size=500000, g=100, ag=[20, 20, 20], init=50000, p=[], w=0, omit=1000, lb_path=None,
        labels_map=labels_map, output_name='enh/enh_HYPERPLANE1_D', sel_series=['CL', 'CL+FX', 'CL+ER'],
        legend=False, xticks=True, ylabel='D', title=None, yticks=[0.0, 0.10, 0.20, 0.30, 0.40], ylim=[-0.02, 0.44])

    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\enh\\enh_TREE4_kappa.dat',
        size=1200000, g=100, ag=[25, 35, 25], init=120000, p=[400000, 800000], w=100000, omit=1000, lb_path=None,
        labels_map=labels_map, output_name='enh/enh_TREE4_kappa', sel_series=['CL', 'CL+FX', 'CL+ER'],
        legend=True, xticks=False, ylabel='Kappa', title='TREE4', yticks=[0.10, 0.40, 0.70, 1.00], ylim=[0.08, 1.05])
    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\enh\\enh_TREE4_DF.dat',
        size=1200000, g=100, ag=[25, 40, 25], init=120000, p=[400000, 800000], w=100000, omit=1000, lb_path=None,
        labels_map=labels_map, output_name='enh/enh_TREE4_DF', sel_series=['CL', 'CL+FX', 'CL+ER'],
        legend=False, xticks=True, ylabel='DF', title=None, yticks=[0.10, 0.30, 0.50, 0.70])


def generate_cmb():
    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\cmb\\cmb_SEA2_acc.dat',
        size=600000, g=100, ag=[35, 35, 35], init=60000, p=[150000, 300000, 450000], w=10000, omit=5000, lb_path=None,
        labels_map=labels_map, output_name='cmb/cmb_SEA2_acc', sel_series=['CLD+FX', 'CLS+FX', 'CLD+ER'],
        legend=True, xticks=False, ylabel='Accuracy', title='SEA2', yticks=[0.60, 0.70, 0.80, 0.90, 1.00], ylim=[0.58, 1.02])
    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\cmb\\cmb_SEA2_DF.dat',
        size=600000, g=100, ag=[25, 25, 25], init=60000, p=[150000, 300000, 450000], w=10000, omit=5000, lb_path=None,
        labels_map=labels_map, output_name='cmb/cmb_SEA2_DF', sel_series=['CLD+FX', 'CLS+FX', 'CLD+ER'],
        legend=False, xticks=True, ylabel='DF', title=None, yticks=[0.00, 0.10, 0.20, 0.30, 0.40], ylim=[-0.02, 0.42])

    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\cmb\\cmb_TREE2_kappa.dat',
        size=1000000, g=100, ag=[30, 30, 30], init=100000, p=[250000, 500000, 750000], w=10000, omit=1000, lb_path=None,
        labels_map=labels_map, output_name='cmb/cmb_TREE2_kappa', sel_series=['CLD+ER', 'CLS+ER', 'BAG-S'],
        legend=True, xticks=False, ylabel='Kappa', title='TREE2', yticks=[0.00, 0.25, 0.50, 0.75, 1.00], ylim=[-0.02, 1.02])
    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\cmb\\cmb_TREE2_D.dat',
        size=1000000, g=100, ag=[20, 15, 20], init=100000, p=[250000, 500000, 750000], w=10000, omit=1000, lb_path=None,
        labels_map=labels_map, output_name='cmb/cmb_TREE2_D', sel_series=['CLD+ER', 'CLS+ER', 'BAG-S'],
        legend=False, xticks=True, ylabel='D', title=None, yticks=[0.00, 0.10, 0.20, 0.30, 0.40], ylim=[-0.02, 0.42])

    # pgfplot.create_plot(
    #     input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\cmb\\cmb_RBF3_acc.dat',
    #     size=1200000, g=100, ag=[35, 35, 35], init=120000, p=[400000, 800000], w=50000, omit=80000, lb_path=None,
    #     labels_map=labels_map, output_name='cmb/cmb_RBF3_acc', sel_series=['CLD+SE', 'CLD+SR', 'BAG-L'],
    #     legend=True, xticks=False, ylabel='Accuracy', title='RBF3')
    # pgfplot.create_plot(
    #     input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\cmb\\cmb_RBF3_D.dat',
    #     size=1200000, g=100, ag=[35, 35, 35], init=120000, p=[400000, 800000], w=50000, omit=80000, lb_path=None,
    #     labels_map=labels_map, output_name='cmb/cmb_RBF3_D', sel_series=['CLD+SE', 'CLD+SR', 'BAG-L'],
    #     legend=False, xticks=True, ylabel='D', title=None)
    #
    # pgfplot.create_plot(
    #     input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\cmb\\cmb_HYPERPLANE2_acc.dat',
    #     size=500000, g=100, ag=[25, 25, 20, 20], init=50000, p=[], w=0, omit=1000, lb_path=None,
    #     labels_map=labels_map, output_name='cmb/cmb_HYPERPLANE2_acc', sel_series=['CLD+SE', 'CLS+SE', 'CLD+SR', 'CLS+SR'],
    #     legend=True, xticks=False, ylabel=None, title='HYPER2')
    # pgfplot.create_plot(
    #     input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\cmb\\cmb_HYPERPLANE2_D.dat',
    #     size=500000, g=100, ag=[25, 25, 20, 20], init=50000, p=[], w=0, omit=1000, lb_path=None,
    #     labels_map=labels_map, output_name='cmb/cmb_HYPERPLANE2_D', sel_series=['CLD+SE', 'CLS+SE', 'CLD+SR', 'CLS+SR'],
    #     legend=False, xticks=True, ylabel=None, title=None)


def generate_rng():
    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\rng\\rng_RBF2_acc.dat',
        size=1000000, g=100, ag=[30, 20], init=100000, p=[250000, 500000, 750000], w=10000, omit=5000, lb_path=None,
        labels_map=labels_map, output_name='rng/rng_RBF2_acc', sel_series=['SCL', 'CL'],
        legend=True, xticks=False, ylabel='Accuracy', title='RBF2')
    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\rng\\rng_RBF2_DF.dat',
        size=1000000, g=100, ag=[20, 20], init=100000, p=[250000, 500000, 750000], w=10000, omit=5000, lb_path=None,
        labels_map=labels_map, output_name='rng/rng_RBF2_DF', sel_series=['SCL', 'CL'],
        legend=False, xticks=True, ylabel='DF', title=None)

    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\rng\\rng_TREE4_kappa.dat',
        size=1200000, g=100, ag=[25, 15], init=120000, p=[400000, 800000], w=100000, omit=1000, lb_path=None,
        labels_map=labels_map, output_name='rng/rng_TREE4_kappa', sel_series=['SCL', 'CL'],
        legend=True, xticks=False, ylabel='Kappa', title='TREE4', yticks=[0.10, 0.30, 0.50, 0.70, 0.90])
    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\rng\\rng_TREE4_D.dat',
        size=1200000, g=100, ag=[15, 15], init=120000, p=[400000, 800000], w=100000, omit=1000, lb_path=None,
        labels_map=labels_map, output_name='rng/rng_TREE4_D', sel_series=['SCL', 'CL'],
        legend=False, xticks=True, ylabel='D', title=None)


def generate_lbs():
    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\lbs\\lbs_TREE_CLL.dat',
        size=1000000, g=100, ag=[1, 1, 1, 1], init=100000, p=[], w=0, omit=0, lb_path=None,
        labels_map=labels_map, output_name='lbs/lbs_TREE_acc', sel_series=['CL-1', 'CL-2', 'CL-3', 'CL-4'],
        legend=True, xticks=True, ylabel='Accuracy', title='TREE')

    pgfplot.create_plot(
        input_path='D:\\Computer_Science\\PhD\\Papers\\2018_BigDataIEEE\\data\\synth\\lbs\\lbs_RBF_CLL.dat',
        size=1000000, g=100, ag=[1, 1, 1, 1], init=100000, p=[], w=0, omit=0, lb_path=None,
        labels_map=labels_map, output_name='lbs/lbs_RBF_kappa', sel_series=['CL-1', 'CL-2', 'CL-3', 'CL-4'],
        legend=True, xticks=True, ylabel='Kappa', title='RBF')


def main():
    print("Running...")
    #generate_ctrl()
    #generate_enh()
    generate_cmb()
    #generate_rng()
    # generate_lbs()


if __name__ == "__main__":
    main()
