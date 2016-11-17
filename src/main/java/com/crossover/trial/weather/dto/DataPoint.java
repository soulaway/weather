package com.crossover.trial.weather.dto;

/**
 * A collected point, including some information about the range of collected
 * values
 *
 * @author code test administrator
 */
public class DataPoint {

	private double mean;

	private int first;

	private int second;

	private int third;

	private int count;
	
	public DataPoint(double mean, int first, int second, int third, int count){
		this.mean = mean;
		this.first = first;
		this.second = second;
		this.third = third;
		this.count = count;
	}
	
	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public int getThird() {
		return third;
	}

	public void setThird(int third) {
		this.third = third;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DataPoint dp = (DataPoint) o;

		return true && mean == dp.getMean() && first == dp.getFirst() && second == dp.getSecond()
				&& third == dp.getThird() && count == dp.getCount();
	}

	@Override
	public int hashCode() {
		return Double.hashCode(mean) + first + second + third + count;
	}

}
