package yasc.gui;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import javax.swing.event.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import yasc.gui.auxiliar.HtmlPane;

public class TreeHelp extends JFrame implements TreeSelectionListener {
    private final JEditorPane htmlPane;
    private final JTree tree;
    private URL helpURL;
    private static final boolean DEBUG = false;

    /** Optionally play with line styles.  Possible values are
     * "Angled" (the default), "Horizontal", and "None".
     */
    private static final boolean playWithLineStyle = false;
    private static final String lineStyle = "Horizontal";
    
    public TreeHelp() {
        setTitle("Help");
	setMinimumSize(new java.awt.Dimension(700, 400));
	Image imagem = Toolkit.getDefaultToolkit().getImage( getClass().getResource("imagens/Logo_iSPD_25.png"));
	setIconImage(imagem);

        // Create the nodes.
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("This Project");
        createNodes(top);

        // Create a tree that allows one selection at a time.
        tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);

        // Listen for when the selection changes.
        tree.addTreeSelectionListener(getInstance());

        if (playWithLineStyle) {
            System.out.println("line style = " + lineStyle);
            tree.putClientProperty("JTree.lineStyle", lineStyle);
        }

        // Create the scroll pane and add the tree to it. 
        JScrollPane treeView = new JScrollPane(tree);

        // Create the HTML viewing pane.
        htmlPane = new HtmlPane();
        initHelp();
        JScrollPane htmlView = new JScrollPane(htmlPane);

        // Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(treeView);
        splitPane.setRightComponent(htmlView);

        Dimension minimumSize = new Dimension(50, 50);
        htmlView.setMinimumSize(minimumSize);
        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(200); 
        splitPane.setPreferredSize(new Dimension(700, 400));

        // Add the split pane to this panel.
        add(splitPane);
    }
    
    private TreeHelp getInstance() {
        return this;
    }

    /** Required by TreeSelectionListener interface. */
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           tree.getLastSelectedPathComponent();

        if (node == null) return;

        Object nodeInfo = node.getUserObject();
        if (node.isLeaf()) {
            BookInfo book = (BookInfo)nodeInfo;
            displayURL(book.bookURL);
            if (DEBUG) {
                System.out.print(book.bookURL + ":  \n    ");
            }
        } else {
            displayURL(helpURL); 
        }
        if (DEBUG) {
            System.out.println(nodeInfo.toString());
        }
    }

    private class BookInfo {
        public String bookName;
        public URL bookURL;

        public BookInfo(String book, String filename) {
            bookName = book;
            bookURL = getClass().getResource(filename);
            if (bookURL == null) {
                System.err.println("Couldn't find the file: " + filename);
            }
        }

        @Override
        public String toString() {
            return bookName;
        }
    }

    private void initHelp() {
        String s = "html/HelpStart.html";
        helpURL = getClass().getResource(s);
        if (helpURL == null) {
            System.err.println("Couldn't open help file: " + s);
        } else if (DEBUG) {
            System.out.println("Help URL is " + helpURL);
        }

        displayURL(helpURL);
    }

    private void displayURL(URL url) {
        try {
            if (url != null) {
                htmlPane.setPage(url);
            } else { //null url
		htmlPane.setText("File Not Found");
                if (DEBUG) {
                    System.out.println("Attempted to display a null URL.");
                }
            }
        } catch (IOException e) {
            System.err.println("Attempted to read a bad URL: " + url);
        }
    }

    private void createNodes(DefaultMutableTreeNode top) {
        DefaultMutableTreeNode category;
        DefaultMutableTreeNode book;

        category = new DefaultMutableTreeNode("Interface");
        top.add(category);

        book = new DefaultMutableTreeNode(new BookInfo("Icons","html/icones.html"));
        category.add(book);

    }
}
