package Outils;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * <b>PanelImage est la classe qui permet de définir un panel sur lequel on peut afficher une image en arrière plan</b>
 * <p>
 * Cette classe est caractérisée par les informations suivantes :
 * <ul>
 * <li>L'image à afficher dans le Panel</li>
 * </ul>
 * </p>
 * <p>
 * De plus, cette classe définit des méthodes permettant d'accéder à l'image en arrière plan et de la modifier.
 * </p>
 * <p>
 * Cette classe hérite de JPanel, il est donc possible d'utiliser toutes les méthodes de cette classe. En
 * particulier, on peut ajouter des composants au premier plans (par dessus l'image).
 * </p>
 * <p>
 * L'image qui est affichée en arrière plan est toujours maximisée dans le JPanel. Pour définir
 * la taille de l'image, on peut fixer la taille du JPanel grâce à la méthode setBounds() ou bien
 * en utilisant des layout.
 * </p>
 * 
 * @author Julien
 * @version 1.0
 *
 */
public class PanelImage extends JPanel {

	/**
	 * L'identifiant de sérialisation
	 */
	private static final long serialVersionUID = -6350324456764768311L;

	private Image image;

	/**
	 * <b>Constructeur de PanelImage</b>
	 */
	public PanelImage() {
		super();
	}
	
	/**
	 * <b>Constructeur de PanelImage</b>
	 * @param image
	 * 	L'image à afficher en arrière plan
	 */
	public PanelImage(Image image) {
		super();
		setImage(image);
	}

	/**
	 * <b>Constructeur de PanelImage</b>
	 * @param path
	 * 	Le chemin de l'image à afficher en arrière plan
	 * @throws IOException si le chemin n'est pas trouvé
	 */
	public PanelImage(String path) throws IOException {
		super();
		setImage(path);
	}
	
	/**
	 * Méthode qui permet de modifier l'image
	 * @param image
	 * 	La nouvelle image à afficher
	 */
	public void setImage(Image image) {
		this.image = image;
		repaint();
	}
	
	/**
	 * Méthode qui permet de modifier l'image
	 * @param path
	 * 	Le chemin vers la nouvelle image à afficher
	 * @throws IOException si le chemin n'est pas trouvé
	 */
	public void setImage(String path) throws IOException {
		try {
			this.image = ImageIO.read(new File(path));
			repaint();
		} 
		catch (IOException e) {
			throw new IOException(path+" introuvable", e);
		}
	}

	/**
	 * Méthode d'accès à l'image courante
	 * @return l'image affichée en arrière plan
	 */
	public Image getImage() {
		return image;
	}
	
	@Override
	public void paintComponent(Graphics g){
		if(image!=null){
			Graphics2D g2d = (Graphics2D)g;
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		}
	}

}
