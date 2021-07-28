package controller;

import java.awt.Color;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;

import model.EstatesModel;
import model.Model;
import view.*;

public class FrontController {
	public static void main(String[] args) throws IOException, ParseException {
		new GirisEkrani();
	}
}
