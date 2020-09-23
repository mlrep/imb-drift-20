import matplotlib.pyplot as plt
import vis.latex_config as latex
from matplotlib.ticker import FormatStrFormatter, FuncFormatter

green1 = '#00BB00'
blue1 = '#0066CC'
red1 = '#DD0000'
yellow1 = '#F1C239'

colors = [red1, blue1, green1, yellow1]


def format_fn(tick_val, tick_pos):
    if tick_val == 0:
        return str(0)
    if tick_val < 1000000:
        return str(int(tick_val / 1000)) + 'k'
    else:
        return str(int(tick_val / 1000000)) + 'm'


def plot_lin(data, fp):
    latex.set_imbalance_streams_config()
    print('Generating plot for {0}'.format(fp))

    for key, values in data.items():
        if key in ['2', '20', '48', '49']:
            x = range(0, len(values))
            y = values
            plt.plot(x, y, label=key, marker='', linewidth=0.5)

    #plt.legend()

    title = fp.split('/')[-1]
    plt.title(title.replace('-D1', '').replace('_', '-'), y=0.95)

    ax = plt.gca()
    ax.grid(color='gray', linestyle='dashed', linewidth=0.4, alpha=0.2)

    ax.yaxis.set_major_formatter(FormatStrFormatter('%.2f'))
    ax.set_ylim([-0.005, 0.036])

    ax.xaxis.set_major_formatter(FuncFormatter(format_fn))
    ax.tick_params(axis='y', pad=3)
    ax.tick_params(axis='x', pad=3)

    plt.grid(True)
    plt.savefig('{0}.pdf'.format(fp), bbox_inches='tight', pad_inches=0.0)
    plt.clf()

