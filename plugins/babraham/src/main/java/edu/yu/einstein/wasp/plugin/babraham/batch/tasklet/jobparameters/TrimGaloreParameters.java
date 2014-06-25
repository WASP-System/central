package edu.yu.einstein.wasp.plugin.babraham.batch.tasklet.jobparameters;

public class TrimGaloreParameters {

    public String adapter = "AGATCGGAAGAGC";
    public String adapter2 = "AGATCGGAAGAGC";

    public int stringency = 1;
    public double error = 0.01;
    public int length = 20;
    public int clip_r1 = 0;
    public int clip_r2 = 0;
    public boolean paired = false;

    public static final String TRIM_GALORE_PARAMETERS = "trim_galore_params";
    public static final String FIRST_FILE_ID = "first_file";

    @Override
    public String toString() {
        String retval = " -a " + adapter;
        if (paired)
            retval += " --paired -a2 " + adapter2;
        retval += " --stringency " + stringency;
        retval += " -e " + error;
        if (clip_r1 != 0)
            retval += " --clip_R1 " + clip_r1;
        if (clip_r2 != 0)
            retval += " --clip_R2 " + clip_r2;

        retval += " ";

        return retval;
    }

    public String getAdapter() {
        return adapter;
    }

    public void setAdapter(String adapter) {
        this.adapter = adapter;
    }

    public String getAdapter2() {
        return adapter2;
    }

    public void setAdapter2(String adapter2) {
        this.adapter2 = adapter2;
        setPaired(true);
    }

    public int getStringency() {
        return stringency;
    }

    public void setStringency(int stringency) {
        this.stringency = stringency;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getClip_r1() {
        return clip_r1;
    }

    public void setClip_r1(int clip_r1) {
        this.clip_r1 = clip_r1;
    }

    public int getClip_r2() {
        return clip_r2;
    }

    public void setClip_r2(int clip_r2) {
        this.clip_r2 = clip_r2;
    }

	public boolean isPaired() {
		return paired;
	}

	public void setPaired(boolean paired) {
		this.paired = paired;
	}

}
