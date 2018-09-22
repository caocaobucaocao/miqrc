package model;

public class Record {
	// 事务号
	public int trans;
	// 商品在此次事务中的消费综合
	public int priceSum;

	public Record(int trans, int priceSum) {
		super();
		this.trans = trans;
		this.priceSum = priceSum;
	}

	public int getTrans() {
		return trans;
	}

	public void setTrans(int trans) {
		this.trans = trans;
	}

	public int getPriceSum() {
		return priceSum;
	}

	public void setPriceSum(int priceSum) {
		this.priceSum = priceSum;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (!(obj instanceof Record)) {
			return false;
		}
		Record record = (Record) obj;
		// && record.priceSum == this.priceSum
		if (record.trans == this.trans) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		int result = 17;
		result = 31 * result + this.trans;
		return result;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Record[ " + "trans:" + trans + "," + "priceSum:" + priceSum + "]";
	}
}
