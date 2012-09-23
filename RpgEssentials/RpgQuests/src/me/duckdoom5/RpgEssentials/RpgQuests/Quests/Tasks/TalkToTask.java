package me.duckdoom5.RpgEssentials.RpgQuests.Quests.Tasks;

import com.topcat.npclib.entity.NPC;

public class TalkToTask extends Task{
	private static final long serialVersionUID = -5657034567850167694L;
	private NPC talkTo;
	private String text;
	private int id;
	
	public TalkToTask(int id, NPC talkTo, String text, NPC taskGiver, NPC taskEnder) {
		super(id, TaskType.TALKTO, taskGiver, taskEnder);
		this.text = text;
		this.id = id;
		this.talkTo = talkTo;
	}
	
	public NPC getNpcToTalkTo(){
		return talkTo;
	}
	
	public String getText(){
		return text;
	}
	
	public int getId(){
		return id;
	}

	public void setNpcToTalkTo(NPC talkTo) {
		this.talkTo = talkTo;
	}

	public void setText(String text) {
		this.text = text;
	}
}
