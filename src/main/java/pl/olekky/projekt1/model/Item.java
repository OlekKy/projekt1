package pl.olekky.projekt1.model;

public class Item {
	
	private int id;
	private String name;
	private int attack;
	private int defense;
	
	public Item() {}
	public Item(int id, String name, int attack, int defense) {
		super();
		this.id = id;
		this.name = name;
		this.attack = attack;
		this.defense = defense;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAttack() {
		return attack;
	}
	public void setAttack(int attack) {
		this.attack = attack;
	}
	public int getDefense() {
		return defense;
	}
	public void setDefense(int defense) {
		this.defense = defense;
	}
}
