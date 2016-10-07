package com.example.ronny.meshmemnode.UILogica;

//Importaciones
import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import java.util.ArrayList;
import com.example.ronny.meshmemnode.R;

/**
 * Clase que se encarga de crear la ctividad que muestra el grafico de la informacion del nodo
 */
public class Informacion  extends Activity {

    /**
     * Metodo que crea los componentes basicos de la actividad, crea el grafico.
     * @param savedInstanceState instancia donde se ejecuta
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);

        PieChart pieChart = (PieChart) findViewById(R.id.pieChart);
        /*definimos algunos atributos*/
        assert pieChart != null;
        pieChart.setHoleRadius(40f);
        pieChart.setDrawYValues(true);
        pieChart.setDrawXValues(true);
        pieChart.setRotationEnabled(true);
        pieChart.animateXY(1500, 1500);

		/*creamos una lista para los valores Y*/
        ArrayList<Entry> valsY = new ArrayList<>();
        valsY.add(new Entry(MainActivity.bytesDisponibles * 100 / MainActivity.bytesTotal ,0));
        valsY.add(new Entry((MainActivity.bytesTotal-MainActivity.bytesDisponibles) * 100 / MainActivity.bytesTotal,1));

 		/*creamos una lista para los valores X*/
        ArrayList<String> valsX = new ArrayList<>();
        valsX.add("Disponible:  " + MainActivity.bytesDisponibles);
        valsX.add("En Uso:  " + (MainActivity.bytesTotal-MainActivity.bytesDisponibles));

 		/*creamos una lista de colores*/
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.colorPrimaryDark));
        colors.add(Color.RED);

 		/*seteamos los valores de Y y los colores*/
        PieDataSet set1 = new PieDataSet(valsY, "Resultados");
        set1.setSliceSpace(3f);
        set1.setColors(colors);

		/*seteamos los valores de X*/
        PieData data = new PieData(valsX, set1);
        pieChart.setData(data);
        pieChart.highlightValues(null);
        pieChart.invalidate();

        /*Ocutar descripcion*/
        pieChart.setDescription("");
        /*Ocultar leyenda*/
        pieChart.setDrawLegend(false);
    }
}