package sp.phone.bean;

import java.util.Set;

public class User {

	// public static final int STATUS_NORMAL = 0;// 正常
	// public static final int STATUS_NUKED = 1;// nuked
	// public static final int STATUS_GAG = 2;// 禁言

	// private long id;
	private String userId;// 编号
	// private String userName;// 登录名
	// private String passWord;// 密码
	private String nickName;// 昵称
	private String avatarImage;// 头像图片地址
	// private int articleNumber;// 发帖数
	// private String signature;// 签名
	// private int status;// 状态
	// private int money;// 金钱
	// private String lastLoginTime;// 最后上线时间
	// private Set<Badge> setBadge;
	// private int level;// 等级
	// private String lastPostTime;// 最后发帖时间
	// private int reputation;// 声望

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAvatarImage() {
		return avatarImage;
	}

	public void setAvatarImage(String avatarImage) {
		this.avatarImage = avatarImage;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
