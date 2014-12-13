package com.project.main;

public abstract class Transition
{
	private long startTime;
	private boolean isComplete;
	
	public Transition()
	{
		startTime = System.nanoTime();
	};
	
	public float progress()
	{
		float progress = (float)(System.nanoTime() - startTime) / (float)(1000 * 1000 * 1000) / duration();
		if (progress > 1 && ! isComplete)
		{
			this.completion();
			isComplete = true;
		}
		
		return Math.min(1, progress);
	}
	
	public boolean isCompleted()
	{
		return isComplete;
	}
	
	protected abstract float duration();
	protected abstract void completion();
}