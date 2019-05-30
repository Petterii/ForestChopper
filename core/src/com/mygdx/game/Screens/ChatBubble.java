package com.mygdx.game.Screens;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.ForestChopper;

import static com.mygdx.game.ForestChopper.PPM;

public class ChatBubble
{
    private Label textLabel;
    private BitmapFont font;
    private Label.LabelStyle lStyle;
    private float scaledWidth = 0;
    private float scaledHeight = 0;
    private Timer.Task currentTask;
    private Texture bkg;

    public ChatBubble()
    {
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        bkg = new Texture("pictures/knob.png");

        NinePatch np = new NinePatch(bkg,11,11,9,10);
        NinePatchDrawable npd = new NinePatchDrawable(np);
        lStyle = new Label.LabelStyle(font,font.getColor());
        lStyle.background = npd;

        textLabel = new Label("",lStyle);
        textLabel.setVisible(false);
        textLabel.setAlignment(Align.center);
        currentTask = new Timer.Task() {
            @Override
            public void run() {
                textLabel.setVisible(false);
            }};
    }

    public void show(String text, float duration)
    {

        if(currentTask.isScheduled())currentTask.cancel();
        textLabel.setText(text);
        textLabel.pack();
        textLabel.setVisible(true);
        scaledHeight = (int)textLabel.getPrefHeight();
        scaledWidth = (int)textLabel.getWidth()/2;
        Timer.schedule(currentTask,duration);
    }

    public void show(String text)
    {

        if(currentTask.isScheduled())currentTask.cancel();
        textLabel.setText(text);
        textLabel.pack();
        textLabel.setVisible(true);
        scaledHeight = (textLabel.getPrefHeight());
        scaledWidth = (textLabel.getWidth()/2);
        Timer.schedule(currentTask,(float)(text.length()*0.1));
    }

    public boolean draw(SpriteBatch batch, float x, float y)
    {
        if(!textLabel.isVisible())return false;
        textLabel.setPosition(x,y);
        batch.begin();
        textLabel.draw(batch, 1);
        batch.end();
        return true;
    }

    public boolean isVisible(){
        return textLabel.isVisible();
    }
}
