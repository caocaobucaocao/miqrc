package model;

import java.util.ArrayList;
import java.util.List;

public class XiangJi implements Comparable<XiangJi> {
	private List<Integer> productIds;

	public XiangJi(List<Integer> productIds) {
		this.productIds = productIds;
	}

	public XiangJi(XiangJi xiangJi) {
		// TODO Auto-generated constructor stub
		List<Integer> data = new ArrayList<>(xiangJi.getProductIds());
		this.productIds = data;
	}

	public List<Integer> getProductIds() {
		return productIds;
	}

	public void setProductIds(List<Integer> productIds) {
		this.productIds = productIds;
	}

	public XiangJi union(XiangJi xiangJi) {
		List<Integer> data = new ArrayList<>();
		for (Integer integer : this.productIds) {
			data.add(integer);
		}
		for (Integer integer : xiangJi.productIds) {
			if (!data.contains(integer))
				data.add(integer);
		}
		return new XiangJi(data);
	}

	public XiangJi intersect(XiangJi xiangJi) {
		List<Integer> data = new ArrayList<>();
		for (int j = 0; j < this.productIds.size(); j++) {
			for (int i = 0; i < xiangJi.getProductIds().size(); i++) {
				if (this.productIds.get(j) == xiangJi.getProductIds().get(i)) {
					data.add(this.productIds.get(j));
				}
			}
		}
		return new XiangJi(data);
	}

	@Override
	public int compareTo(XiangJi o) throws ClassCastException, IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		for (int i = 0; i < productIds.size(); i++) {
			List<Integer> temp = o.productIds;
			if (temp.get(i) != productIds.get(i))
				return temp.get(i) - productIds.get(i);
		}
		return 0;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return productIds.toString();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj instanceof XiangJi) {
			XiangJi temp = (XiangJi) obj;
			if (temp.productIds.size() == this.productIds.size()) {
				for (int i = 0; i < productIds.size(); i++) {
					if (temp.productIds.get(i) != this.productIds.get(i)) {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		int resullt = 17;
		for (int i = 0; i < productIds.size(); i++) {
			resullt = resullt * 31 + productIds.get(i);
		}
		return resullt;
	}
}
