package utilz;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class LoadSave {
    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";

    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTON = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String BACKGROUND_MENU = "background_menu.png";
    public static final String PLAYING_BG_IMG = "playing_bg_img.png";
    public static final String THE_BIG_CLOUD = "big_clouds.png";
    public static final String THE_SMALL_CLOUD = "small_clouds.png";
    public static final String CRABBY_SPRITE = "crabby_sprite.png";
    public static final String STATUS_BAR = "health_power_bar.png";
    public static final String COMPLETED_IMG = "completed_sprite.png";
    public static final String lvl1_IMG = "1.png";


    public static BufferedImage GetSpriteAtlas(String filename){

        InputStream is = LoadSave.class.getResourceAsStream("/" + filename);
        BufferedImage img = null;
        try {

            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return img;
        }
    }
    public static BufferedImage[] GetAllLevels() {
        URL url = LoadSave.class.getResource("/lvls");
        File file = null;

        try {
            file = new File(url.toURI());
        }catch (URISyntaxException e){
            e.printStackTrace();
        }
        File[] files = file.listFiles();
        File[] fileSorted = new File[files.length];

        for (int i = 0; i < fileSorted.length; i++)
            for (int j = 0; j < files.length; j++) {
                if (files[j].getName().equals((i + 1) + ".png"))
                    fileSorted[i] = files[j];
            }

        BufferedImage[] imgs = new BufferedImage[fileSorted.length];
        for (int i = 0; i < imgs.length; i++)
            try{
                imgs[i] = ImageIO.read(fileSorted[i]);
            }catch (IOException e){
                e.printStackTrace();
            }

        return imgs;
    }
}
