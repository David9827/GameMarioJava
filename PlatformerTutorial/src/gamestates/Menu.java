package gamestates;

import main.Game;
import ui.MenuButton;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Menu extends State implements Statemethods{
    private int menuX, menuY, menuWidth, menuHeight;
    private BufferedImage backgroundImg, backgroundPic;
    private MenuButton[] buttons = new MenuButton[3];
    public Menu(Game game) {
        super(game);
        loadButtons();
        loadBackground();
        backgroundPic = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_MENU);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
        menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
        menuX = (int) ((Game.GAME_WIDTH / 2) - (menuWidth / 2));
        menuY = (int) (45 * Game.SCALE);
    }

    private void loadButtons() {
        buttons[0] = new MenuButton(Game.GAME_WIDTH/2, (int) (150*Game.SCALE), 0, Gamestate.PLAYING);
        buttons[1] = new MenuButton(Game.GAME_WIDTH/2, (int) (220*Game.SCALE), 1, Gamestate.OPTIONS);
        buttons[2] = new MenuButton(Game.GAME_WIDTH/2, (int) (290*Game.SCALE), 2, Gamestate.QUIT);
    }

    @Override
    public void update() {
        for (MenuButton mb : buttons)
            mb.update();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundPic, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);

        for (MenuButton mb : buttons)
            mb.draw(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb : buttons){
            if (isIn(e,mb)){
                mb.setMousePressed(true);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton mb : buttons){
            if (isIn(e, mb)){
                if (mb.isMousePressed())
                    mb.applyGameState();
                break;
            }
        }
        resetButton();
    }

    private void resetButton() {
        for (MenuButton mb : buttons)
            mb.resetBounds();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton mb : buttons)
            mb.setMouseOver(false);
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)){
                mb.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            Gamestate.state = Gamestate.PLAYING;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
