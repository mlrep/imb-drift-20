import numpy as np
from files import reader
import matplotlib as mpl
mpl.use('pgf')
import matplotlib.pyplot as plt
from matplotlib.ticker import FormatStrFormatter

green1 = '#00BB00'
blue1 = '#0066CC'
red1 = '#DD0000'
yellow1 = '#F1C239'

colors = [red1, blue1, green1, yellow1]
markers = ['', '', '', '']
line_width = [0.7, 0.6, 0.5, 0.5]

output_scale = 0.49
column_width = 243.911
text_width = 505.89


def figsize(scale):
    fig_width_pt = column_width  # Get this from LaTeX using \the\textwidth
    inches_per_pt = 1.0 / 72.27  # Convert pt to inch
    golden_mean = (np.sqrt(5.0) - 1.0) / 2.0  # Aesthetic ratio (you could change this)
    fig_width = fig_width_pt * inches_per_pt * scale  # width in inches
    fig_height = fig_width * 0.8 #* golden_mean
    fig_size = [fig_width, fig_height]

    return fig_size


pgf_with_latex = {
    "pgf.texsystem": "pdflatex",
    "text.usetex": True,
    "font.family": "serif",
    "font.serif": [],  # blank entries should cause plots to inherit fonts from the document
    "font.sans-serif": [],
    "font.monospace": [],
    "axes.labelsize": int(8),
    "font.size": int(10),
    "legend.fontsize": int(5),
    "xtick.labelsize": int(7),
    "ytick.labelsize": int(7),
    "axes.titlesize": int(7),
    "figure.figsize": figsize(output_scale),
    "pgf.preamble": [
        r"\usepackage{times}"
    ]
}
mpl.rcParams.update(pgf_with_latex)


def create_new_fig():
    plt.clf()
    fig = plt.figure()
    ax = fig.add_subplot(111)

    return fig, ax


def save_fig(filename):
    #plt.savefig('{}.pgf'.format(filename), bbox_inches='tight')
    plt.savefig('{}.pdf'.format(filename), bbox_inches='tight', pad_inches=0.0)


def create_plot(input_path, sel_series, size, g, ag, init, p, w, omit, lb_path, labels_map, output_name, legend,
                xticks, ylabel, title, yticks=None, ylim=None):
    print('Generating: {0}'.format(output_name))
    series = reader.read_series(input_path)
    fig, ax = create_new_fig()
    inv_map = {v: k for k, v in labels_map.items()}

    i = 0
    omit_val = 0

    if p is not None:
        for pp in p:
            # plt.axvline(x=p, linewidth=0.1, color='black')
            plt.axvline(x=pp - w, linewidth=0.3, color='black', linestyle='dashed', alpha=0.5)
            plt.axvline(x=pp + w, linewidth=0.3, color='black', linestyle='dashed', alpha=0.5)

    for alg in sel_series:
        vals = series[inv_map[alg]]
        x = list(map(lambda x: (x * g) + init, range(0, len(vals))))

        og = int(omit/g)
        omit_val = x[og]

        ax.plot(x[og::ag[i]], vals[og::ag[i]], linewidth=line_width[i], color=colors[i], marker=markers[i], markersize=0.1,
                label=alg)
        i = i + 1

    # if lb_path is not None:
    #     lb_series = reader.read_series(lb_path)
    #     label = list(lb_series.keys())[0]
    #     lb_vals = lb_series[label]
    #     x = list(map(lambda x: (x * g) + init, range(0, len(lb_vals))))
    #
    #     og = int(omit / g)
    #     omit_val = x[og]
    #
    #     ax.plot(x[og::1], lb_vals[og::1], linewidth=0.3, color=yellow1, marker=markers[i], markersize=0.1, label='lb')

    #ax.set_xlabel('Instance')

    if ylabel is not None:
        ax.set_ylabel(ylabel, labelpad=5)
    else:
        ax.set_ylabel(' ')

    if legend:
        plt.legend(loc='upper center', bbox_to_anchor=(0.5, 1.2), ncol=4, frameon=False, columnspacing=0.75, handletextpad=0.4)

    if title is not None:
        plt.title(title, y=1.15)

    axes = plt.gca()
    axes.set_xlim([omit_val, size])
    if ylim is not None:
        axes.set_ylim(ylim)

    ax.yaxis.set_major_formatter(FormatStrFormatter('%.2f'))
    if yticks is not None:
        plt.yticks(yticks)

    ticks = []
    if size == 500000:
        ticks = range(100000, 500000, 150000)
    elif size == 600000:
        ticks = range(100000, 600000, 200000)
    elif size == 1000000:
        ticks = range(300000, 1000000, 300000)
    elif size == 1200000:
        ticks = range(200000, 1100000, 400000)

    ax.tick_params(axis='y', pad=3)
    ax.tick_params(axis='x', pad=3)

    ticks_labels = []
    for tic in ticks:
        if tic < 1000000:
            ticks_labels.append(str(int(tic/1000)) + 'k')
        else:
            ticks_labels.append(str(int(tic/1000000)) + 'm')

    plt.xticks(ticks, ticks_labels)
    ax.grid(color='gray', linestyle='dashed', linewidth=0.4, alpha=0.2)
    plt.grid(True)


    save_fig('results/{0}'.format(output_name))
