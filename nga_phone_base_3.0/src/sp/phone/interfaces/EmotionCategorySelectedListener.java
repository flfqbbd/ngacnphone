package sp.phone.interfaces;

public interface EmotionCategorySelectedListener {
	public final int CATEGORY_BASIC = 0;
	public final int CATEGORY_BAOZOU = 1;
	public final int CATEGORY_ALI = 2;
	public final int CATEGORY_DAYANMAO = 3;
	public final int CATEGORY_LUOXIAOHEI = 4;
	public final int CATEGORY_ZHAIYIN = 5;
	public final int CATEGORY_YANGCONGTOU = 6;
	public final int CATEGORY_ACNIANG = 7;
	public final int CATEGORY_BIERDE= 8;
	
	void onEmotionCategorySelected(int category);
}
