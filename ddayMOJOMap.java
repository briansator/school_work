/**
Brian Sator
CS 537
Lab 3

Of note:
Much of this code is taken directly from the QuickStart9e example from class.
I used this example as a base for the sole purpose of having a pre-written
workable GIS application on which I would build my GIS feature.  As such
I will highlight briefly here the code that I have added in order to create
my layer moving functionality:

-Added additional layers for testing and demonstration purposes.
-Added a JMenuItem(orderLayers) to the layercontrol JMenu
-Modified the layercontrollis to listen for clicks from my new JMenuItem
-Created the class layerOrderDialog which is the largest portion of the
 code for my layer mover feature.  This class holds the code for the
 dialogbox UI allowing users to reorder layers, as well as the code that
 does the actual layer ordering itself.

 Functionality: Provides dropdown menus of current layers to user, allowing
 them to select a specific layer and choose another layer to move the first
 above or below.  Reordering takes place and the drop-down menu choices are 
 updated appropriately.

**/
package ddayMOJOMap;
import javax.swing.*;
import java.io.IOException;
import java.io.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Vector;
import java.util.StringTokenizer;
import com.esri.mo2.ui.bean.*; // beans used: Map,Layer,Toc,TocAdapter,
        // TocEvent,Legend(a legend is part of a toc)
import com.esri.mo2.ui.tb.ZoomPanToolBar;
import com.esri.mo2.ui.tb.SelectionToolBar;
import com.esri.mo2.ui.ren.LayerProperties;
import com.esri.mo2.cs.geom.Envelope;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import com.esri.mo2.data.feat.*; //ShapefileFolder, ShapefileWriter
import com.esri.mo2.map.dpy.FeatureLayer;
import com.esri.mo2.file.shp.*;
import com.esri.mo2.map.dpy.Layerset;
import com.esri.mo2.map.draw.*;
import com.esri.mo2.ui.bean.Tool;
import java.util.StringTokenizer;
import com.esri.mo2.map.draw.SimpleMarkerSymbol;
import com.esri.mo2.map.draw.BaseSimpleRenderer;
import com.esri.mo2.map.dpy.BaseFeatureLayer;
import java.awt.geom.*;
import com.esri.mo2.cs.geom.*;
import java.util.ArrayList;
public class ddayMOJOMap extends JFrame {
  static Map map = new Map();
  static boolean fullMap = true;  // Map not zoomed
  Legend legend;
  Legend legend2;
  Layer layer = new Layer();
  Layer layer2 = new Layer();
  Layer layer3 = new Layer();
  Layer layer5 = new Layer();
  Layer layer6 = new Layer();
  Layer layer7 = null;
  static com.esri.mo2.map.dpy.Layer layer4;
  com.esri.mo2.map.dpy.Layer activeLayer;
  int activeLayerIndex;
  JMenuBar mbar = new JMenuBar();
  JMenu file = new JMenu("File");
  JMenu theme = new JMenu("Theme");
  JMenu layercontrol = new JMenu("LayerControl");
  JMenuItem attribitem = new JMenuItem("open attribute table",
                            new ImageIcon("tableview.gif"));
  JMenuItem createlayeritem  = new JMenuItem("create layer from selection",
                    new ImageIcon("Icon0915b.jpg"));
  static JMenuItem promoteitem = new JMenuItem("promote selected layer",
                    new ImageIcon("promote1.gif"));
  JMenuItem demoteitem = new JMenuItem("demote selected layer",
                    new ImageIcon("demote1.gif"));
  JMenuItem orderLayers = new JMenuItem("Order Map Layers");
  JMenuItem printitem = new JMenuItem("print",new ImageIcon("print.gif"));
  JMenuItem addlyritem = new JMenuItem("add layer",new ImageIcon("addtheme.gif"));
  JMenuItem remlyritem = new JMenuItem("remove layer",new ImageIcon("delete.gif"));
  JMenuItem propsitem = new JMenuItem("Legend Editor",new ImageIcon("properties.gif"));
  Toc toc = new Toc();
  String s1 = "C:\\Users\\satorb\\Documents\\CS537\\esri\\MOJ20\\Samples\\Data\\Europe\\France.shp";
  String datapathname = "";
  String legendname = "";
  ZoomPanToolBar zptb = new ZoomPanToolBar();
  static SelectionToolBar stb = new SelectionToolBar();
  JToolBar jtb = new JToolBar();
  ComponentListener complistener;
  JLabel statusLabel = new JLabel("status bar    LOC");
  java.text.DecimalFormat df = new java.text.DecimalFormat("0.000");
  JPanel myjp = new JPanel();
  JPanel myjp2 = new JPanel();
  JButton prtjb = new JButton(new ImageIcon("print.gif"));
  JButton addlyrjb = new JButton(new ImageIcon("addtheme.gif"));
  JButton ptrjb = new JButton(new ImageIcon("pointer.gif"));
  JButton hotjb = new JButton(new ImageIcon("hotlink-small.gif"));
  Toolkit tk = Toolkit.getDefaultToolkit();
  Image bolt = tk.getImage("hotlink.gif");
  java.awt.Cursor boltCursor = tk.createCustomCursor(bolt,new java.awt.Point(6,25),"bolt");
  Arrow arrow = new Arrow();
  ActionListener lis;
  ActionListener layerlis;
  ActionListener layercontrollis;
  TocAdapter mytocadapter;
  MyPickAdapter picklis = new MyPickAdapter();
  Identify hotlink = new Identify();
  class MyPickAdapter implements PickListener{
    public void beginPick(PickEvent pe){}
    public void endPick(PickEvent pe){}
    public void foundData(PickEvent pe){
      com.esri.mo2.data.feat.Cursor cursor = pe.getCursor();
      String name,wiki,image;
      while (cursor.hasMore()){
        Feature f = (com.esri.mo2.data.feat.Feature)cursor.next();
        name = f.getValue(2).toString();
        wiki = f.getValue(3).toString();
        image = f.getValue(4).toString();
        try{
          HotPick hotpick = new HotPick(name,wiki,image);
          hotpick.setVisible(true);
        }catch(Exception e){}
      }
      System.out.println("Feature Clicked");
    }
  };
  static Envelope env;
  public ddayMOJOMap() {
    super("Brian Sator's Lab 3");
    this.setSize(1050,750);
    zptb.setMap(map);
    stb.setMap(map);
    setJMenuBar(mbar);
    ActionListener lisZoom = new ActionListener() {
          public void actionPerformed(ActionEvent ae){
            fullMap = false;}}; // can change a boolean here
        ActionListener lisFullExt = new ActionListener() {
          public void actionPerformed(ActionEvent ae){
            fullMap = true;}};
        // next line gets ahold of a reference to the zoomin button
        JButton zoomInButton = (JButton)zptb.getActionComponent("ZoomIn");
        JButton zoomFullExtentButton =
                (JButton)zptb.getActionComponent("ZoomToFullExtent");
        JButton zoomToSelectedLayerButton =
              (JButton)zptb.getActionComponent("ZoomToSelectedLayer");
        zoomInButton.addActionListener(lisZoom);
        zoomFullExtentButton.addActionListener(lisFullExt);
        zoomToSelectedLayerButton.addActionListener(lisZoom);
        complistener = new ComponentAdapter () {
          public void componentResized(ComponentEvent ce) {
            if(fullMap) {
              map.setExtent(env);
              map.zoom(1.0);    //scale is scale factor in pixels
              map.redraw();
            }
          }
        };
    addComponentListener(complistener);
    lis = new ActionListener() {public void actionPerformed(ActionEvent ae){
          Object source = ae.getSource();
          if (source == prtjb || source instanceof JMenuItem ) {
        com.esri.mo2.ui.bean.Print mapPrint = new com.esri.mo2.ui.bean.Print();
        mapPrint.setMap(map);
        mapPrint.doPrint();// prints the map
        }
      else if (source == ptrjb) {
                map.setSelectedTool(arrow);
            }
            else if (source == hotjb){
              map.setSelectedTool(hotlink);
              hotlink.setCursor(boltCursor);
            }
          else
            {
                try {
              AddLyrDialog aldlg = new AddLyrDialog();
              aldlg.setMap(map);
              aldlg.setVisible(true);
            } catch(IOException e){}
      }
    }};
    layercontrollis = new ActionListener() {public void
                actionPerformed(ActionEvent ae){
          String source = ae.getActionCommand();
          System.out.println(activeLayerIndex+" active index");
          if (source == "promote selected layer")
                map.getLayerset().moveLayer(activeLayerIndex,++activeLayerIndex);
          else if (source =="Order Map Layers"){
                LayerOrderDialog orderDialog = new LayerOrderDialog(map);
                orderDialog.setVisible(true);
              }
          else
                map.getLayerset().moveLayer(activeLayerIndex,--activeLayerIndex);
      enableDisableButtons();
      map.redraw();
    }};
    layerlis = new ActionListener() {public void actionPerformed(ActionEvent ae){
          Object source = ae.getSource();
          if (source instanceof JMenuItem) {
                String arg = ae.getActionCommand();
                if(arg == "add layer") {
          try {
                AddLyrDialog aldlg = new AddLyrDialog();
                aldlg.setMap(map);
                aldlg.setVisible(true);
          } catch(IOException e){}
              }
            else if(arg == "remove layer") {
              try {
                        com.esri.mo2.map.dpy.Layer dpylayer =
                           legend.getLayer();
                        map.getLayerset().removeLayer(dpylayer);
                        map.redraw();
                        remlyritem.setEnabled(false);
                        propsitem.setEnabled(false);
                        attribitem.setEnabled(false);
                        promoteitem.setEnabled(false);
                        demoteitem.setEnabled(false);
                        stb.setSelectedLayer(null);
                        zptb.setSelectedLayer(null);
                        stb.setSelectedLayers(null);
                  } catch(Exception e) {}
              }
            else if(arg == "Legend Editor") {
          LayerProperties lp = new LayerProperties();
          lp.setLegend(legend);
          lp.setSelectedTabIndex(0);
          lp.setVisible(true);
            }
            else if (arg == "open attribute table") {
              try {
                layer4 = legend.getLayer();
            AttrTab attrtab = new AttrTab();
            attrtab.setVisible(true);
              } catch(IOException ioe){}
            }
        else if (arg=="create layer from selection") {
              com.esri.mo2.map.draw.BaseSimpleRenderer sbr = new
                com.esri.mo2.map.draw.BaseSimpleRenderer();
                  com.esri.mo2.map.draw.SimplePolygonSymbol simplepolysymbol = new
                    com.esri.mo2.map.draw.SimplePolygonSymbol();// for polygons
                  simplepolysymbol.setPaint(AoFillStyle.getPaint(
                        com.esri.mo2.map.draw.AoFillStyle.SOLID_FILL,new
                    java.awt.Color(255,255,0)));
                  simplepolysymbol.setBoundary(true);
              layer4 = legend.getLayer();
              FeatureLayer flayer2 = (FeatureLayer)layer4;
              // select, e.g., Montana and then click the
              // create layer menuitem; next line verifies a selection was made
              System.out.println("has selected" + flayer2.hasSelection());
              //next line creates the 'set' of selections
              if (flayer2.hasSelection()) {
                    SelectionSet selectset = flayer2.getSelectionSet();
                // next line makes a new feature layer of the selections
                FeatureLayer selectedlayer = flayer2.createSelectionLayer(selectset);
                sbr.setLayer(selectedlayer);
                sbr.setSymbol(simplepolysymbol);
                selectedlayer.setRenderer(sbr);
                Layerset layerset = map.getLayerset();
                // next line places a new visible layer, e.g. Montana, on the map
                layerset.addLayer(selectedlayer);
                //selectedlayer.setVisible(true);
                if(stb.getSelectedLayers() != null)
                  promoteitem.setEnabled(true);
                try {
                  legend2 = toc.findLegend(selectedlayer);
                    } catch (Exception e) {}

                    CreateShapeDialog csd = new CreateShapeDialog(selectedlayer);
                    csd.setVisible(true);
                Flash flash = new Flash(legend2);
                flash.start();
                map.redraw(); // necessary to see color immediately

                  }
            }
      }
    }};
    toc.setMap(map);
    mytocadapter = new TocAdapter() {
          public void click(TocEvent e) {
            legend = e.getLegend();
            activeLayer = legend.getLayer();
            com.esri.mo2.map.dpy.Layer tempLayer = activeLayer;
            stb.setSelectedLayer(activeLayer);
            zptb.setSelectedLayer(activeLayer);
            // get active layer index for promote and demote
            activeLayerIndex = map.getLayerset().indexOf(activeLayer);
            com.esri.mo2.map.dpy.Layer[] layers = {tempLayer};
            hotlink.setSelectedLayers(layers);
            remlyritem.setEnabled(true);
            propsitem.setEnabled(true);
            attribitem.setEnabled(true);
            enableDisableButtons();
          }
    };
    map.addMouseMotionListener(new MouseMotionAdapter() {
          public void mouseMoved(MouseEvent me) {
                com.esri.mo2.cs.geom.Point worldPoint = null;
                if (map.getLayerCount() > 0) {
                  worldPoint = map.transformPixelToWorld(me.getX(),me.getY());
                  String s = "X:"+df.format(worldPoint.getX())+" "+
                             "Y:"+df.format(worldPoint.getY());
                  statusLabel.setText(s);
              }
            else
              statusLabel.setText("X:0.000 Y:0.000");
      }
    });
    toc.addTocListener(mytocadapter);
    remlyritem.setEnabled(false); // assume no layer initially selected
    propsitem.setEnabled(false);
    attribitem.setEnabled(false);
    promoteitem.setEnabled(false);
    demoteitem.setEnabled(false);
    printitem.addActionListener(lis);
    addlyritem.addActionListener(layerlis);
    remlyritem.addActionListener(layerlis);
    propsitem.addActionListener(layerlis);
    attribitem.addActionListener(layerlis);
    createlayeritem.addActionListener(layerlis);
    promoteitem.addActionListener(layercontrollis);
    demoteitem.addActionListener(layercontrollis);
    orderLayers.addActionListener(layercontrollis);
    file.add(addlyritem);
    file.add(printitem);
    file.add(remlyritem);
    file.add(propsitem);
    theme.add(attribitem);
    theme.add(createlayeritem);
    layercontrol.add(promoteitem);
    layercontrol.add(demoteitem);
    layercontrol.add(orderLayers);
    mbar.add(file);
    mbar.add(theme);
    mbar.add(layercontrol);
    prtjb.addActionListener(lis);
    prtjb.setToolTipText("print map");
    addlyrjb.addActionListener(lis);
    addlyrjb.setToolTipText("add layer");
    hotlink.addPickListener(picklis);
    hotlink.setPickWidth(5);
    hotjb.addActionListener(lis);
    hotjb.setToolTipText("hotlink tool");
    ptrjb.addActionListener(lis);
    prtjb.setToolTipText("pointer");
    jtb.add(hotjb);
    jtb.add(prtjb);
    jtb.add(addlyrjb);
    jtb.add(ptrjb);
    myjp.add(jtb);
    myjp.add(zptb); myjp.add(stb);
    myjp2.add(statusLabel);
    getContentPane().add(map, BorderLayout.CENTER);
    getContentPane().add(myjp,BorderLayout.NORTH);
    getContentPane().add(myjp2,BorderLayout.SOUTH);
    addShapefileToMap(layer,s1);
    try{
      AddXYtheme addXYtheme = new AddXYtheme();
      addXYtheme.setMap(map);
    }catch(IOException e){}
    //buildCoordinatesLayer();
    getContentPane().add(toc, BorderLayout.WEST);
    map.zoom(1.0);
  }
  private void addShapefileToMap(Layer layer,String s) {
    String datapath = s; //"C:\\ESRI\\MOJ10\\Samples\\Data\\USA\\States.shp";
    layer.setDataset("0;"+datapath);
    map.add(layer);
  }
  class AddXYtheme extends JDialog{
    Map map;
    Vector tempVec = new Vector();
    BasePointsArray bpa = new BasePointsArray();
    AddXYtheme() throws IOException{
      try{
        File file = new File("C:/Users/satorb/Documents/CS537/esri/MOJ20/ddayMOJOMap/ddaycoords.csv");
        FileReader freader = new FileReader(file);
        BufferedReader in = new BufferedReader(freader);
        String s;
        double x,y;
        int n = 0;
        while((s = in.readLine()) != null){
          StringTokenizer st = new StringTokenizer(s,",");
          y = Double.parseDouble(st.nextToken());
          x = Double.parseDouble(st.nextToken());
          bpa.insertPoint(n, new com.esri.mo2.cs.geom.Point(x,y));
          tempVec.addElement(st.nextToken());
          tempVec.addElement(st.nextToken());
          tempVec.addElement(st.nextToken());
        }
      }catch(IOException e){}
      XYfeatureLayer xyfl = new XYfeatureLayer(bpa,map,tempVec);
      xyfl.setVisible(true);
      map = ddayMOJOMap.map;
      map.getLayerset().addLayer(xyfl);
      map.redraw();
    }
    public void setMap(com.esri.mo2.ui.bean.Map tempMap){
      map = tempMap;
    }
  }
  class XYfeatureLayer extends BaseFeatureLayer{
    BaseFields fields;
    private java.util.Vector featureVector;
    public XYfeatureLayer(BasePointsArray bpa, Map map, Vector tempVec){
      createFeatures(bpa,map,tempVec);
      BaseFeatureClass bfc = getFeatureClass("Landing Points",bpa);
      setFeatureClass(bfc);
      BaseSimpleRenderer srd = new BaseSimpleRenderer();
      SimpleMarkerSymbol sms = new SimpleMarkerSymbol();
      sms.setType(SimpleMarkerSymbol.CIRCLE_MARKER);
      sms.setSymbolColor(new Color(255,0,0));
      sms.setWidth(7);
      srd.setSymbol(sms);
      setRenderer(srd);
      XYLayerCapabilities lc = new XYLayerCapabilities();
      setCapabilities(lc);
    }
    private void createFeatures(BasePointsArray bpa, Map map, Vector tempVec){
      featureVector = new java.util.Vector();
      fields = new BaseFields();
      createDbfFields();
        for(int i = 0; i < bpa.size(); i++){
          BaseFeature feature = new BaseFeature();
          feature.setFields(fields);
          com.esri.mo2.cs.geom.Point p = new com.esri.mo2.cs.geom.Point(bpa.getPoint(i));
          feature.setValue(0,p);
          feature.setValue(1,new Integer(i));
          feature.setValue(2,(String)tempVec.elementAt(i*3));
          feature.setValue(3,(String)tempVec.elementAt(i*3+1));
          feature.setValue(4,(String)tempVec.elementAt(i*3+2));
          feature.setDataID(new BaseDataID("LandingPoints", i));
          featureVector.addElement(feature);
        }
    }
    private void createDbfFields(){
      fields.addField(new BaseField("#SHAPE", Field.ESRI_SHAPE,0,0));
      fields.addField(new BaseField("ID", java.sql.Types.INTEGER,9,0));
      fields.addField(new BaseField("Name",java.sql.Types.VARCHAR,16,0));
      fields.addField(new BaseField("Wiki",java.sql.Types.VARCHAR,32,0));
      fields.addField(new BaseField("Image",java.sql.Types.VARCHAR,32,0));
    }
    public BaseFeatureClass getFeatureClass(String name, BasePointsArray bpa){
      com.esri.mo2.map.mem.MemoryFeatureClass featClass = null;
      try{
        featClass = new com.esri.mo2.map.mem.MemoryFeatureClass(MapDataset.POINT,fields);
      } catch(IllegalArgumentException iae){}
      featClass.setName(name);
      for(int i=0;i<bpa.size();i++){
        featClass.addFeature((Feature) featureVector.elementAt(i));
      }
      return featClass;
    }
    private final class XYLayerCapabilities extends com.esri.mo2.map.dpy.LayerCapabilities{
      XYLayerCapabilities(){
        for(int i=0;i<this.size();i++){
          setAvailable(this.getCapabilityName(i),true);
          setEnablingAllowed(this.getCapabilityName(i),true);
          getCapability(i).setEnabled(true);
        }
      }
    }
  }

  class HotPick extends JDialog{
    String wiki;
    JButton wikiBtn = new JButton("Wiki Page");
    HotPick(String tempName, String tempWiki, String tempImage) throws IOException{
      setTitle("Landing Point");
      setBounds(50,50,650,450);
      wiki = tempWiki;
      JLabel nameLabel = new JLabel(tempName);
      nameLabel.setFont(new Font("Tahoma", Font.BOLD,18));
      JLabel imageLabel = new JLabel();
      imageLabel.setIcon(new ImageIcon(tempImage));
      wikiBtn.addActionListener(wikiLis);
      getContentPane().add(nameLabel,BorderLayout.NORTH);
      getContentPane().add(imageLabel,BorderLayout.CENTER);
      getContentPane().add(wikiBtn,BorderLayout.SOUTH);
      addWindowListener(new WindowAdapter(){
        public void windowClosing(WindowEvent e){
          setVisible(false);
        }
      });
    }
    ActionListener wikiLis = new ActionListener(){
      public void actionPerformed(ActionEvent ae){
        Object source = ae.getSource();
        if(source == wikiBtn){
          try {
           Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + wiki);
          } catch (Exception e) {}
       }
      }
    };

  }
  public static void main(String[] args) {
    ddayMOJOMap qstart = new ddayMOJOMap();
    qstart.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
            System.out.println("Thanks, Kab 3 exits");
            System.exit(0);
        }
    });
    qstart.setVisible(true);
    env = map.getExtent();
  }
  private void enableDisableButtons() {
    int layerCount = map.getLayerset().getSize();
    if (layerCount < 2) {
      promoteitem.setEnabled(false);
      demoteitem.setEnabled(false);
      }
    else if (activeLayerIndex == 0) {
      demoteitem.setEnabled(false);
      promoteitem.setEnabled(true);
          }
    else if (activeLayerIndex == layerCount - 1) {
      promoteitem.setEnabled(false);
      demoteitem.setEnabled(true);
          }
        else {
          promoteitem.setEnabled(true);
          demoteitem.setEnabled(true);
    }
  }
}

class LayerOrderDialog extends JDialog{
  ActionListener listener;
  Map map;
  JPanel panel1 = new JPanel();
  JPanel panel2 = new JPanel();
  JPanel panel3 = new JPanel();
  JPanel panel4 = new JPanel();
  JButton cancel = new JButton("Cancel");
  JButton update = new JButton("Move");
  JLabel label1 = new JLabel("Choose Layer to Move:");
  JLabel label2 = new JLabel("Choose Layer to Move Above/Below:");
  JLabel label3 = new JLabel("Above or Below:");
  String[] appLayers;
  String[] appMoveOptions = {"Above", "Below"};
  JComboBox flayers;
  JComboBox ab = new JComboBox(appMoveOptions);
  JComboBox tlayers;
  
  LayerOrderDialog(com.esri.mo2.ui.bean.Map map1){
    map = map1;
    setBounds(150,50,250,250);
    setTitle("Change Layer Order");
    addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e){
        setVisible(false);
      }
    });
    listener = new ActionListener(){
      public void actionPerformed(ActionEvent ae){
        Object source = ae.getSource();
        if(source == cancel){
          setVisible(false);
        }
        else{
          customizedMoveLayer(map);
        }
      }
    };
    
    int layerSize = map.getLayerset().getSize();
    String[] appLayers = new String[layerSize];
    for(int i=0; i<layerSize; i++){
      String tempS = map.getLayerset().layerAt(i).getName();
      appLayers[i] = tempS;
    }

    flayers = new JComboBox(appLayers);
    tlayers = new JComboBox(appLayers);
    getContentPane().setLayout(new GridLayout(0,1));
    cancel.addActionListener(listener);
    update.addActionListener(listener);
    panel1.setLayout(new GridLayout(0,1));
    panel2.setLayout(new GridLayout(0,1));
    panel3.setLayout(new GridLayout(0,1));
    panel4.add(cancel);
    panel4.add(update);
    panel1.add(label1);
    panel1.add(flayers);
    panel2.add(label3);
    panel2.add(ab);
    panel3.add(label2);
    panel3.add(tlayers);
    getContentPane().add(panel1);
    getContentPane().add(panel2);
    getContentPane().add(panel3);
    getContentPane().add(panel4);
  };

  private void customizedMoveLayer(com.esri.mo2.ui.bean.Map map1){
    int moveIndex = flayers.getSelectedIndex();
    int toIndex = tlayers.getSelectedIndex();
    int abIndex = ab.getSelectedIndex();
    int layerSize = map1.getLayerset().getSize();
    String[] appLayers = new String[layerSize];
    if(abIndex == 0){
       if(toIndex == 0){
          map1.getLayerset().moveLayer(moveIndex, 1);
       }else{
          map1.getLayerset().moveLayer(moveIndex,toIndex);
       }
       for(int i=0; i<layerSize; i++){
         String tempS = map1.getLayerset().layerAt(i).getName();
         appLayers[i] = tempS;
       }
       flayers.setModel(new JComboBox(appLayers).getModel());
       tlayers.setModel(new JComboBox(appLayers).getModel());

    }else{
      if(toIndex == 0){
         map1.getLayerset().moveLayer(moveIndex,toIndex);
      }else{
         map1.getLayerset().moveLayer(moveIndex,--toIndex);
      }
      for(int i=0; i<layerSize; i++){
         String tempS = map1.getLayerset().layerAt(i).getName();
         appLayers[i] = tempS;
       }
       flayers.setModel(new JComboBox(appLayers).getModel());
       tlayers.setModel(new JComboBox(appLayers).getModel());
    }
  }

}

// following is an Add Layer dialog window
class AddLyrDialog extends JDialog {
  Map map;
  ActionListener lis;
  JButton ok = new JButton("OK");
  JButton cancel = new JButton("Cancel");
  JPanel panel1 = new JPanel();
  com.esri.mo2.ui.bean.CustomDatasetEditor cus = new com.esri.mo2.ui.bean.
    CustomDatasetEditor();
  AddLyrDialog() throws IOException {
        setBounds(50,50,520,430);
        setTitle("Select a theme/layer");
        addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            setVisible(false);
          }
        });
        lis = new ActionListener() {
          public void actionPerformed(ActionEvent ae) {
            Object source = ae.getSource();
            if (source == cancel)
              setVisible(false);
            else {
              try {
                        setVisible(false);
                        map.getLayerset().addLayer(cus.getLayer());
                        map.redraw();
                        if (ddayMOJOMap.stb.getSelectedLayers() != null){
                          ddayMOJOMap.promoteitem.setEnabled(true);}
                  } catch(IOException e){}
            }
          }
      };
    ok.addActionListener(lis);
    cancel.addActionListener(lis);
    getContentPane().add(cus,BorderLayout.CENTER);
    panel1.add(ok);
    panel1.add(cancel);
    getContentPane().add(panel1,BorderLayout.SOUTH);
  }
  public void setMap(com.esri.mo2.ui.bean.Map map1){
        map = map1;
  }
}

class AttrTab extends JDialog {
  JPanel panel1 = new JPanel();
  com.esri.mo2.map.dpy.Layer layer = ddayMOJOMap.layer4;
  JTable jtable = new JTable(new MyTableModel());
  JScrollPane scroll = new JScrollPane(jtable);

  public AttrTab() throws IOException {
        setBounds(70,70,450,350);
        setTitle("Attribute Table");
        addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            setVisible(false);
          }
    });
    scroll.setHorizontalScrollBarPolicy(
           JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        // next line necessary for horiz scrollbar to work
        jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableColumn tc = null;
        int numCols = jtable.getColumnCount();
        //jtable.setPreferredScrollableViewportSize(
                //new java.awt.Dimension(440,340));
        for (int j=0;j<numCols;j++) {
          tc = jtable.getColumnModel().getColumn(j);
          tc.setMinWidth(50);
    }
    getContentPane().add(scroll,BorderLayout.CENTER);
  }
}
class MyTableModel extends AbstractTableModel {
 // the required methods to implement are getRowCount,
 // getColumnCount, getValueAt
  com.esri.mo2.map.dpy.Layer layer = ddayMOJOMap.layer4;
  MyTableModel() {
        qfilter.setSubFields(fields);
        com.esri.mo2.data.feat.Cursor cursor = flayer.search(qfilter);
        while (cursor.hasMore()) {
                ArrayList inner = new ArrayList();
                Feature f = (com.esri.mo2.data.feat.Feature)cursor.next();
                inner.add(0,String.valueOf(row));
                for (int j=1;j<fields.getNumFields();j++) {
                  inner.add(f.getValue(j).toString());
                }
            data.add(inner);
            row++;
    }
  }
  FeatureLayer flayer = (FeatureLayer) layer;
  FeatureClass fclass = flayer.getFeatureClass();
  String columnNames [] = fclass.getFields().getNames();
  ArrayList data = new ArrayList();
  int row = 0;
  int col = 0;
  BaseQueryFilter qfilter = new BaseQueryFilter();
  Fields fields = fclass.getFields();
  public int getColumnCount() {
        return fclass.getFields().getNumFields();
  }
  public int getRowCount() {
        return data.size();
  }
  public String getColumnName(int colIndx) {
        return columnNames[colIndx];
  }
  public Object getValueAt(int row, int col) {
          ArrayList temp = new ArrayList();
          temp =(ArrayList) data.get(row);
      return temp.get(col);
  }
}
class CreateShapeDialog extends JDialog {
  String name = "";
  String path = "";
  JButton ok = new JButton("OK");
  JButton cancel = new JButton("Cancel");
  JTextField nameField = new JTextField("enter layer name here, then hit ENTER",25);
  com.esri.mo2.map.dpy.FeatureLayer selectedlayer;
  ActionListener lis = new ActionListener() {public void actionPerformed(ActionEvent ae) {
        Object o = ae.getSource();
        if (o == nameField) {
          name = nameField.getText().trim();
          path = ((ShapefileFolder)(ddayMOJOMap.layer4.getLayerSource())).getPath();
          System.out.println(path+"    " + name);
    }
        else if (o == cancel)
      setVisible(false);
        else {
          try {
                ShapefileWriter.writeFeatureLayer(selectedlayer,path,name,2);
          } catch(Exception e) {System.out.println("write error");}
          setVisible(false);
    }
  }};

  JPanel panel1 = new JPanel();
  JLabel centerlabel = new JLabel();
  //centerlabel;
  CreateShapeDialog (com.esri.mo2.map.dpy.FeatureLayer layer5) {
        selectedlayer = layer5;
    setBounds(40,350,450,150);
    setTitle("Create new shapefile?");
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
            setVisible(false);
          }
    });
    nameField.addActionListener(lis);
    ok.addActionListener(lis);
    cancel.addActionListener(lis);
    String s = "<HTML> To make a new shapefile from the new layer, enter<BR>" +
      "the new name you want for the layer and click OK.<BR>" +
      "You can then add it to the map in the usual way.<BR>"+
      "Click ENTER after replacing the text with your layer name";
    centerlabel.setHorizontalAlignment(JLabel.CENTER);
    centerlabel.setText(s);
    getContentPane().add(centerlabel,BorderLayout.CENTER);
    panel1.add(nameField);
    panel1.add(ok);
    panel1.add(cancel);
    getContentPane().add(panel1,BorderLayout.SOUTH);
  }
}
class Arrow extends Tool {
  public void mouseClicked(MouseEvent me){
  }
}
class Flash extends Thread {
  Legend legend;
  Flash(Legend legendin) {
        legend = legendin;
  }
  public void run() {
        for (int i=0;i<12;i++) {
          try {
                Thread.sleep(500);
                legend.toggleSelected();
          } catch (Exception e) {}
    }
  }
}
