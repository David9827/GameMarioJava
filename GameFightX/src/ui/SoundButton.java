package ui;

import utilz.LoadSave;
import static utilz.Constants.UI.SoundButton.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SoundButton extends PauseButton{
    private BufferedImage[][] soundImg;
    private boolean mouseMover, mousePressed;
    private boolean muted;
    private int rowIndex, colIndex;
    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);
        loadSoundButton();
    }

    private void loadSoundButton() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SOUND_BUTTON);
        soundImg = new BufferedImage[2][3];
        for (int j = 0; j < soundImg.length; j++)
            for (int i =0; i < soundImg[j].length; i++){
                soundImg[j][i] = temp.getSubimage(i * SOUND_SIZE_DEFAULT, j * SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT);
            }
    }
    public void update(){
        if (muted)
            rowIndex = 1;
        else
            rowIndex = 0;
        colIndex = 0;
        if (mouseMover)
            colIndex = 1;
        if (mousePressed)
            colIndex = 2;
    }

    public void resetBools(){
        mouseMover = false;
        mousePressed = false;
    }
    public void draw(Graphics g){
        g.drawImage(soundImg[rowIndex][colIndex ], x,y,width,height,null);
    }

    public boolean isMouseMover() {
        return mouseMover;
    }

    public void setMouseMover(boolean mouseMover) {
        this.mouseMover = mouseMover;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }
}
