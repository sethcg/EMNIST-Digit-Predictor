package deeplearning_test;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class DigitVisual extends GridPane{
	
	private DoubleProperty percent = new SimpleDoubleProperty(this, "percent");
	
	private Label digitLabel = new Label("");
	private ProgressBar progress = new ProgressBar(0.0);
	private Label percentLabel = new Label("");
	
	public DigitVisual(int digit){
		this.percent.setValue(0.0);
		
		digitLabel.setText("" + digit);
		digitLabel.setAlignment(Pos.CENTER);
		progress.progressProperty().bind(percent);
		percentLabel.setText("" + percent.get() + "%");
		percentLabel.setAlignment(Pos.CENTER);

		this.setAlignment(Pos.CENTER);
		this.setPadding(new Insets(0,20,0,20));
		
		ColumnConstraints digitCol = new ColumnConstraints();
		digitCol.setMinWidth(50);
		digitCol.setHgrow(Priority.NEVER);
		digitCol.setHalignment(HPos.CENTER);
	    ColumnConstraints percentCol = new ColumnConstraints();
	    percentCol.setMinWidth(100);
	    percentCol.setHgrow(Priority.NEVER);
	    percentCol.setHalignment(HPos.CENTER);
	    ColumnConstraints progressCol = new ColumnConstraints();
	    progressCol.setHgrow(Priority.ALWAYS);
	    
	    this.getColumnConstraints().addAll(digitCol, percentCol, progressCol);
	    
		this.add(digitLabel, 	0, 0);
		this.add(percentLabel, 	1, 0);
		this.add(progress, 		2, 0);
		
	}
	
	protected void setPercent(double newPercent){
		percent.setValue(newPercent / 100);
		percentLabel.setText("" + newPercent + "%");
	}
}
