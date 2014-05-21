package com.stockcloud;

import java.util.List;

public class handler {
	static int algorithm(List<String> list, int num) {
		String[] data_str = new String[num + 1];
		for (int r = 0; r < (num + 1); r++) {
			data_str[r] = list.get(r);
			System.out.println(data_str[r]);
		}
		float[] array = new float[num + 1];
		for (int p = 0; p < (num + 1); p++) {
			array[p] = Float.parseFloat(data_str[p]);
			// System.out.println(array[p]);

		}

		float mean = 0;
		float mean1 = 0;
		float mean2 = 0;
		float x = 1;
		float[] x2 = new float[num + 1];
		float[] x3 = new float[num + 1];
		float[] temp1 = new float[num + 1];
		float temp1mean = 0;
		float[] temp2 = new float[num + 1];
		float temp2mean = 0;
		float[] temp3 = new float[num + 1];
		float temp3mean = 0;
		float[] temp4 = new float[num + 1];
		float temp4mean = 0;
		float beta3 = 0;
		float beta2 = 0;
		float beta1 = 0;
		float beta0 = 0;
		float result;
		for (int a = 0; a < (num + 1); a++) {
			x2[a] = array[a + num + 1];
		}
		for (int b = 0; b < (num + 1); b++) {
			x3[b] = array[b + 2 * (num + 1)];
		}
		for (int i = 0; i < (num + 1); i++) {
			mean = mean + array[i];
			mean1 = mean1 + x2[i];
			mean2 = mean2 + x3[i];
		}
		mean = mean / (num + 1);
		mean1 = mean1 / (num + 1);
		mean2 = mean2 / (num + 1);
		for (int j = 0; j < (num + 1); j++) {
			temp1[j] = (float) ((x - 15.5) * (array[j] - mean));
			temp1mean = temp1mean + temp1[j];
			temp2[j] = (float) ((x - 15.5) * (x - 15.5));
			temp2mean = temp2mean + temp2[j];
			temp3[j] = (float) ((x - 15.5) * (x2[j] - mean));
			temp3mean = temp3mean + temp1[j];
			temp4[j] = (float) ((x - 15.5) * (x3[j] - mean));
			temp4mean = temp4mean + temp1[j];
			x = x + 1;
		}
		beta3 = temp4mean / temp3mean;
		beta2 = temp3mean * temp4mean;
		beta1 = temp1mean / temp2mean;
		beta0 = (float) (mean - beta1 * 15.5);
		result = beta3 * x3[num + 1] + beta2 * x2[num + 1] + x * beta1 + beta0;
		return (int) (result);

	}
}
