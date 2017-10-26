package pl.olekky.projekt1.model;

public class Player {
	
	private int id;
	private String nickname;
	private int gold;
	public Player() {};
	public Player(int id, String nickname, int gold) {
		this.id = id;
		this.nickname = nickname;
		this.gold = gold;
	}
	
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
}
