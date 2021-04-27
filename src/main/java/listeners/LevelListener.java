package listeners;

public interface LevelListener {
	public void playerDead(int playerHealth);
	public void playerWin();
	public void playerLose();
	public void playerCollect(int coins);
}
