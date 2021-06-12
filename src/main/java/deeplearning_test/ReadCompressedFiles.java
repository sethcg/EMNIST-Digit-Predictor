package deeplearning_test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * An example showing how to use the MNIST reader to read the original,
 * compressed files that are published at
 * <a href="http://yann.lecun.com/exdb/mnist/">
 * http://yann.lecun.com/exdb/mnist/</a>
 */
public class ReadCompressedFiles
{
    /**
     * Entry point of this example
     * 
     * @param args Not used
     * @throws IOException If an IO error occurs
     */
    public static void main(String[] args) throws IOException
    {
        readCompressedFiles();
    }

    /**
     * Read the MNIST data from a local directory, and print 
     * information about the data that is read to the console. 
     * The directory is assumed to contain the original MNIST files.
     * 
     * @throws IOException If an IO error occurs
     */
    private static void readCompressedFiles() throws IOException
    {
        Path inputDirectoryPath = Paths.get("./data");

        MnistCompressedReader mnistReader = new MnistCompressedReader();
        Consumer<MnistEntry> consumer = System.out::println;

        System.out.println("Training data:");
        mnistReader.readCompressedTraining(inputDirectoryPath, consumer);

        System.out.println("Testing data:");
        mnistReader.readCompressedTesting(inputDirectoryPath, consumer);
    }

}