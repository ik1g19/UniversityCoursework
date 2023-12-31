package uk.ac.ecs.soton.ik1g19.ch14;

import org.openimaj.data.dataset.GroupedDataset;
import org.openimaj.data.dataset.ListDataset;
import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.experiment.dataset.sampling.GroupSampler;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.annotation.evaluation.datasets.Caltech101;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.processing.convolution.FGaussianConvolve;
import org.openimaj.image.processing.resize.ResizeProcessor;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.time.Timer;
import org.openimaj.util.function.Operation;
import org.openimaj.util.parallel.Parallel;
import org.openimaj.util.parallel.partition.RangePartitioner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * OpenIMAJ Hello world!
 *
 */
public class AppCh14 {
    public static void main( String[] args ) throws Exception {
        //retrieving and sampling images
        VFSGroupDataset<MBFImage> allImages = Caltech101.getImages(ImageUtilities.MBFIMAGE_READER);
        GroupedDataset<String, ListDataset<MBFImage>, MBFImage> images = GroupSampler.sample(allImages, 8, false);


        //original, un-parallelized code
        /*
        List<MBFImage> output = new ArrayList<MBFImage>();
        ResizeProcessor resize = new ResizeProcessor(200);
        Timer t1 = Timer.timer();
        for (ListDataset<MBFImage> clzImages : images.values()) {
            MBFImage current = new MBFImage(200, 200, ColourSpace.RGB);

            for (MBFImage i : clzImages) {
                MBFImage tmp = new MBFImage(200, 200, ColourSpace.RGB);
                tmp.fill(RGBColour.WHITE);

                MBFImage small = i.process(resize).normalise();
                int x = (200 - small.getWidth()) / 2;
                int y = (200 - small.getHeight()) / 2;
                tmp.drawImage(small, x, y);

                current.addInplace(tmp);
            }
            current.divideInplace((float) clzImages.size());
            output.add(current);
        }

        System.out.println("Time: " + t1.duration() + "ms");
        DisplayUtilities.display("Images", output);

         */


        //parallelized version
        /*
        List<MBFImage> output = new ArrayList<MBFImage>();
        ResizeProcessor resize = new ResizeProcessor(200);
        Timer t1 = Timer.timer();
        for (ListDataset<MBFImage> clzImages : images.values()) {
            MBFImage current = new MBFImage(200, 200, ColourSpace.RGB);

            Parallel.forEachPartitioned(new RangePartitioner<MBFImage>(clzImages), new Operation<Iterator<MBFImage>>() {
                public void perform(Iterator<MBFImage> it) {
                    MBFImage tmpAccum = new MBFImage(200, 200, 3);
                    MBFImage tmp = new MBFImage(200, 200, ColourSpace.RGB);

                    while (it.hasNext()) {
                        final MBFImage i = it.next();
                        tmp.fill(RGBColour.WHITE);

                        final MBFImage small = i.process(resize).normalise();
                        final int x = (200 - small.getWidth()) / 2;
                        final int y = (200 - small.getHeight()) / 2;
                        tmp.drawImage(small, x, y);
                        tmpAccum.addInplace(tmp);
                    }
                    synchronized (current) {
                        current.addInplace(tmpAccum);
                    }
                }
            });
            current.divideInplace((float) clzImages.size());
            output.add(current);
        }

        System.out.println("Time: " + t1.duration() + "ms");
        DisplayUtilities.display("images", output);
         */


        //exercise 1
        //parallelized outer loop
        List<MBFImage> output = new ArrayList<MBFImage>();
        ResizeProcessor resize = new ResizeProcessor(200);
        Parallel.forEach(images.values(), new Operation<ListDataset<MBFImage>>() {
            public void perform(ListDataset<MBFImage> clzImages) {
                MBFImage current = new MBFImage(200, 200, ColourSpace.RGB);

                for (MBFImage i : clzImages) {
                    MBFImage tmp = new MBFImage(200, 200, ColourSpace.RGB);
                    tmp.fill(RGBColour.WHITE);

                    MBFImage small = i.process(resize).normalise();
                    int x = (200 - small.getWidth()) / 2;
                    int y = (200 - small.getHeight()) / 2;
                    tmp.drawImage(small, x, y);

                    current.addInplace(tmp);
                }
                current.divideInplace((float) clzImages.size());
                output.add(current);
            }
        });
    }
}
