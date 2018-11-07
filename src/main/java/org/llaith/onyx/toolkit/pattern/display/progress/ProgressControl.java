package org.llaith.onyx.toolkit.pattern.display.progress;

/**
 *
 */
public class ProgressControl {

    private final boolean verbose;
    private final int scale; // a scaling factor 

    private long count = 0; // the count done
    private long total = 1; // the current total 

    private int complete; // the scaled count done
    private double percent; // the scaled percent done


    public ProgressControl(final boolean verbose, final int scale) {

        this.verbose = verbose;

        this.scale = scale;

    }

    public boolean isVerbose() {
        return verbose;
    }

    public int scale() {
        return scale;
    }

    public long count() {
        return count;
    }

    public long total() {
        return total;
    }

    public int complete() {
        return complete;
    }

    public double percent() {
        return percent;
    }

    public ProgressControl incrementCount(final int count) {

        this.count += count;

        return this;

    }

    public ProgressControl incrementTotal(final int total) {

        this.total += total;

        return this;

    }

    public ProgressControl setCount(final long count) {

        this.count = count;

        return this;

    }

    public ProgressControl setTotal(final long total) {

        this.total = total;

        return this;

    }

    public ProgressControl update() {

        // easier than mucking around with integer casts inline
        final double ddone = (double)this.count;
        final double dtotal = (double)this.total;
        final double factor = this.scale / dtotal;
        final double rawPercent = (100 / dtotal) * ddone;

        // calc the completion
        this.complete = (int)(ddone * factor);

        // calc the percentage
        this.percent = ((rawPercent == 100) && (this.count != this.total)) ?
                rawPercent - 1 :
                rawPercent;

        // use this for debugging to get a clear look without the replaceline interfering
        //System.out.println();

        return this;

    }

}
