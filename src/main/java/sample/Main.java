package sample;

import com.esri.arcgisruntime.geometry.*;
import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.*;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.tasks.offlinemap.PreplannedMapArea;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.List;


public class Main extends Application {

    private MapView mapView;
    private List<PreplannedMapArea> mapAreas;
    private static SpatialReference SPATIAL_REFERENCE = SpatialReferences.getWgs84();


    @Override
    public void start(Stage primaryStage) throws Exception {

        StackPane stackPane = new StackPane();

        ArcGISMap map = new ArcGISMap(Basemap.Type.IMAGERY, 59.915457, 30.3205208, 14);
        System.out.println(map.getLoadStatus());
        ArcGISTiledLayer tiledLayer = new ArcGISTiledLayer("http://services.arcgisonline.com/arcgis/rest/services/World_Imagery/MapServer");
        ArcGISMapImageLayer censusLayer = new ArcGISMapImageLayer("http://sampleserver6.arcgisonline.com/arcgis/rest/services/Census/MapServer");
        map.getBasemap().getBaseLayers().add(tiledLayer);
        map.getOperationalLayers().add(censusLayer);

        ArcGISMapImageLayer usaLayer = new ArcGISMapImageLayer("http://sampleserver6.arcgisonline.com/arcgis/rest/services/USA/MapServer");
        map.getOperationalLayers().add(0, usaLayer);

        mapView = new MapView();
        mapView.setMap(map);

        PointCollection points = new PointCollection(SPATIAL_REFERENCE);

        points.add(new Point(30.3105208, 59.915457));
        points.add(new Point(30.3105208, 59.905457));
        points.add(new Point(30.3405208, 59.915457));
        points.add(new Point(30.3405208, 59.925457));

        Polygon polygon = new Polygon(points);

        // Задаем границу полигона
        SimpleLineSymbol outlineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID,
                0xFF905000, 2);

        // Задаем тип заливки полигона
        SimpleFillSymbol fillSymbol = new
                SimpleFillSymbol(SimpleFillSymbol.Style.CROSS, 0xFF505050,
                outlineSymbol);

        // Добавляем графику на слой.
        Graphic graphic = new Graphic(polygon, fillSymbol);
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        graphicsOverlay.getGraphics().add(graphic);

        // Добавляем графический слой на карту.
        mapView.getGraphicsOverlays().add(graphicsOverlay);

        // Статус загрузки карты
        map.getOperationalLayers().get(0).setOpacity(0.5f);
        map.loadAsync();
        map.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
            System.out.println("Карта загружена");
            }
        });

        stackPane.getChildren().addAll(mapView);
        Scene scene = new Scene(stackPane);

        Camera camera = new Camera(53.06, -4.04, 1289.0, 295.0, 71, 0.0);



        primaryStage.setTitle("Display Map Sample");
        primaryStage.setWidth(800);
        primaryStage.setHeight(700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        if (mapView != null) {
            mapView.dispose();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
