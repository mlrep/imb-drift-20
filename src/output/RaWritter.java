package output;
import eval.experiment.ExperimentResult;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class RaWritter {

    public static void writeResultsToFile(List<ExperimentResult> results, String dirPath, int logGranularity) {
        if (dirPath == null) return;
        HashMap<String, BufferedWriter> seriesOutputWriters, averageOutputWriters;
        HashMap<String, String[]> outputNames = RaWritter.getOutputNames(results);

        try {
            seriesOutputWriters = initOutputWriters(outputNames.get("series"), dirPath + "/series");
            averageOutputWriters = initOutputWriters(outputNames.get("averages"), dirPath + "/averages");

            for (ExperimentResult result : results) {
                String label = result.label + (result.subLabel.isEmpty() ? "" : "#" + result.subLabel);
                writeSeriesMeasurements(seriesOutputWriters, result, label, logGranularity);
                writeAverageMeasurements(averageOutputWriters, result, label);
                //outputWriter.write("Queries:" + String.format("%.4f", result.queriesFactor));
            }

            closeOutputWriters(seriesOutputWriters);
            closeOutputWriters(averageOutputWriters);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, String[]> getOutputNames(List<ExperimentResult> results) {
        HashMap<String, String[]> outputNames = new HashMap<>();
        Set<String> seriesOutputNames = new HashSet<>();
        Set<String> averagesOutputNames = new HashSet<>();

        for (ExperimentResult result : results) {
            seriesOutputNames.addAll(result.seriesMeasurements.keySet());
            averagesOutputNames.addAll(result.averageMeasurements.keySet());
        }

        outputNames.put("series", seriesOutputNames.toArray(new String[0]));
        outputNames.put("averages", averagesOutputNames.toArray(new String[0]));

        return outputNames;
    }

    private static HashMap<String, BufferedWriter> initOutputWriters(String[] outputNames, String dirPath) throws IOException {
        HashMap<String, BufferedWriter> outputWriters = new HashMap<>();
        for (String outputName : outputNames) {
            outputWriters.put(outputName, new BufferedWriter(new FileWriter(dirPath + "/" + outputName + ".data")));
        }

        return outputWriters;
    }

    private static void writeSeriesMeasurements(HashMap<String, BufferedWriter> seriesOutputWriters, ExperimentResult result, String label, int logGranularity) throws IOException {
        for (Map.Entry<String, BufferedWriter> entry : seriesOutputWriters.entrySet()) {
            BufferedWriter outputWriter = entry.getValue();
            List<Double> values = result.seriesMeasurements.get(entry.getKey());

            if (values != null) {
                outputWriter.write(label);
                for (int i = 0; i < values.size(); i++) {
                    if ((i % logGranularity) == 0) outputWriter.write(String.format(",%.4f", values.get(i)));
                }
                outputWriter.newLine();
            }
        }
    }

    private static void writeAverageMeasurements(HashMap<String, BufferedWriter> averageOutputWriters, ExperimentResult result, String label) throws IOException {
        for (Map.Entry<String, BufferedWriter> entry : averageOutputWriters.entrySet()) {
            BufferedWriter outputWriter = entry.getValue();
            double value = result.averageMeasurements.get(entry.getKey());

            outputWriter.write(label);
            outputWriter.write(String.format(",%.4f", value));
            outputWriter.newLine();
        }
    }

    private static void closeOutputWriters(HashMap<String, BufferedWriter> outputWriters) throws IOException {
        for (Map.Entry<String, BufferedWriter> entry : outputWriters.entrySet()) {
            BufferedWriter outputWriter = entry.getValue();
            outputWriter.flush();
            outputWriter.close();
        }
    }

    public static void writeSeriesHashMapToCSV(String outputPath, LinkedHashMap<String, ArrayList<Double>> seriesHashMap) {
        BufferedWriter writer = null;

        try {
            (new File(outputPath).getParentFile()).mkdirs();
            writer = new BufferedWriter(new FileWriter(outputPath, true));

            for (Map.Entry<String, ArrayList<Double>> seriesHashMapEntry : seriesHashMap.entrySet()) {
                String algorithmLabel = seriesHashMapEntry.getKey();
                writer.write(algorithmLabel);

                ArrayList<Double> values = seriesHashMapEntry.getValue();
                for (Double value : values) {
                    writer.write(String.format(",%.4f", value));
                }

                writer.write("\r\n");
            }

        }  catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (writer != null) try { writer.close(); } catch (IOException ex) { ex.printStackTrace(); }
        }
    }

    public static void writeSMAResultsToCSV(String outputPath, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Double>>> smaResults) {
        BufferedWriter writer = null;
        String[] streams = smaResults.keySet().toArray(new String[smaResults.size()]);
        String[] measurements = smaResults.get(streams[0]).keySet().toArray(new String[smaResults.get(streams[0]).size()]);
        String[] algorithms = smaResults.get(streams[0]).get(measurements[0]).keySet().toArray(new String[smaResults.get(streams[0]).get(measurements[0]).size()]);

        try {
            (new File(outputPath).getParentFile()).mkdirs();
            writer = new BufferedWriter(new FileWriter(outputPath, true));

            for (String stream : streams) {
                writer.write(stream + ",");
                writer.write(String.join(",", measurements) + "\r\n");

                for (String algorithm : algorithms) {
                    writer.write(algorithm);

                    for (String measurement : measurements) {
                        System.out.println("Writing " + stream + " " + algorithm + " " + measurement);
                        writer.write(String.format(",%.4f", smaResults.get(stream).get(measurement).get(algorithm)));
                    }

                    writer.write("\r\n");
                }

                writer.write("\r\n");
            }

        }  catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (writer != null) try { writer.close(); } catch (IOException ex) { ex.printStackTrace(); }
        }
    }
}
