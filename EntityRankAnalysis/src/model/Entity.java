package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Entity {

	private String name;
	private int linkToEntityCount;
	private int linkFromEntityCount;
	private HashMap<String, Integer> linkToEntitySet;
	private HashMap<String, Integer> linkFromEntitySet;
	private double entityRankValue;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLinkToCompanyCount() {
		return linkToEntityCount;
	}

	@SuppressWarnings("rawtypes")
	public void setLinkToEntityCount() {
		int count = 0;
		if (linkToEntitySet != null && !linkToEntitySet.isEmpty()) {
			Iterator iterator = linkToEntitySet.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry entry = (Entry) iterator.next();
				count += (int) entry.getValue();
			}
		}
		this.linkToEntityCount = count;
	}

	public int getLinkFromEntityCount() {
		return linkFromEntityCount;
	}

	@SuppressWarnings("rawtypes")
	public void setLinkFromEntityCount() {
		int count = 0;
		if (linkFromEntitySet != null && !linkFromEntitySet.isEmpty()) {
			Iterator iterator = linkFromEntitySet.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry entry = (Entry) iterator.next();
				count += (int) entry.getValue();
			}
		}
		this.linkFromEntityCount = count;
	}

	public void setLinkToEntityCountByIncrement() {
		setLinkToEntityCount();
		// this.linkToCompanyCount+=increment;
	}

	public void setLinkFromEntityCountByIncrement() {
		setLinkFromEntityCount();
		// this.linkFromCompanyCount+=increment;
	}

	public HashMap<String, Integer> getLinkToEntitySet() {
		return linkToEntitySet;
	}

	public void setLinkToEntitySet(HashMap<String, Integer> linkToCompanySet) {
		this.linkToEntitySet = linkToCompanySet;
	}

	public HashMap<String, Integer> getLinkFromEntitySet() {
		return linkFromEntitySet;
	}

	public void setLinkFromEntitySet(HashMap<String, Integer> linkFromCompanySet) {
		this.linkFromEntitySet = linkFromCompanySet;
	}

	public double getEntityRankValue() {
		return entityRankValue;
	}

	public void setEntityRankValue(double companyRankValue) {
		this.entityRankValue = companyRankValue;
	}

	/* testing */
	@Override
	public String toString() {
		return this.name + "\t入链数："
				+ this.linkFromEntityCount + ":" + "\t入集：" + this.getLinkFromEntitySet() + " ,  " + "\t出链数:"
				+ this.linkToEntityCount + "\t出集:" + this.getLinkToEntitySet();
	}

	public String showEntityRank() {
		return this.name + String.format("\t%.12f", this.getEntityRankValue());
	}

}
