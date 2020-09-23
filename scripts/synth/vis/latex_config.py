import numpy as np
import matplotlib as mpl
#mpl.use('pgf')


def figsize(scale, fig_width_pt):
    fig_width_pt = fig_width_pt  # Get this from LaTeX using \the\textwidth
    inches_per_pt = 1.0 / 72.27  # Convert pt to inch
    golden_mean = (np.sqrt(5.0) - 1.0) / 2.0  # Aesthetic ratio (you could change this)
    fig_width = fig_width_pt * inches_per_pt * scale  # width in inches
    fig_height = fig_width * 0.8  # * golden_mean
    fig_size = [fig_width, fig_height]

    return fig_size


def set_diveristy_ensembles_config():
    output_scale = 0.49
    text_width = 347.123
    column_width = 243.911
    text_width = 505.89

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
        "figure.figsize": figsize(output_scale, column_width),
        "pgf.preamble": [
            r"\usepackage{times}"
        ]
    }
    mpl.rcParams.update(pgf_with_latex)


def set_imbalance_streams_config():
    output_scale = 0.24
    text_width = 347.123

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
        "xtick.labelsize": int(5),
        "ytick.labelsize": int(5),
        "axes.titlesize": int(7),
        "figure.figsize": figsize(output_scale, text_width),
        "pgf.preamble": [
            r"\usepackage{times}"
        ]
    }
    mpl.rcParams.update(pgf_with_latex)
