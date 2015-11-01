package com.algaworks.pedidovenda.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.time.DateUtils;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.algaworks.pedidovenda.repository.ConectaMySql;
import com.algaworks.pedidovenda.repository.Produtos;

@Named
@RequestScoped
public class GraficoFluxoCaixaBean {

	public BigDecimal totalVendas() {
		SimpleDateFormat dataFormadata = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.S");
		Calendar dataInicial = Calendar.getInstance();
		Calendar dataFinal = Calendar.getInstance();
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataInicial.add(Calendar.DAY_OF_MONTH, -30);
		String cFirstString = dataFormadata.format(dataInicial.getTime());
		String cEndString = dataFormadata.format(dataFinal.getTime());
		BigDecimal value = BigDecimal.ZERO;
		try {
			Connection con = ConectaMySql.obtemConexao();
			PreparedStatement statement = con
					.prepareStatement("SELECT SUM(valor_total) FROM pedido WHERE data_criacao BETWEEN  ?  and ? AND status = 'EMITIDO' ");
			statement.setString(1, cFirstString);
			statement.setString(2, cEndString);
			ResultSet result = statement.executeQuery();
			result.next();
			String sum = result.getString(1);
			System.out.println(sum);
			value = new BigDecimal(sum);
			con.close();
		} catch (Exception exc) {
			System.out.println(exc.getMessage());
		}

		return value;
	}

	public BigDecimal leftEstoque() {
		SimpleDateFormat dataFormadata = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.S");
		Calendar dataInicial = Calendar.getInstance();
		Calendar dataFinal = Calendar.getInstance();
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataInicial.add(Calendar.DAY_OF_MONTH, -30);
		String cFirstString = dataFormadata.format(dataInicial.getTime());
		String cEndString = dataFormadata.format(dataFinal.getTime());
		BigDecimal value = BigDecimal.ZERO;
		try {
			Connection con = ConectaMySql.obtemConexao();
			PreparedStatement statement = con
					.prepareStatement("SELECT  sum(i.valor_custo * i.quantidade) FROM item_pedido i INNER JOIN pedido p ON p.id = i.pedido_id WHERE p.data_criacao BETWEEN  ?  and  ? ");
			statement.setString(1, cFirstString);
			statement.setString(2, cEndString);
			ResultSet result = statement.executeQuery();
			result.next();
			String sum = result.getString(1);
			System.out.println(sum);
			value = new BigDecimal(sum);
			con.close();
		} catch (Exception exc) {
			System.out.println(exc.getMessage());
		}
		return value;
	}

	public BigDecimal inEstoque() {
		SimpleDateFormat dataFormadata = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.S");
		Calendar dataInicial = Calendar.getInstance();
		Calendar dataFinal = Calendar.getInstance();
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataInicial.add(Calendar.DAY_OF_MONTH, -30);
		String cFirstString = dataFormadata.format(dataInicial.getTime());
		String cEndString = dataFormadata.format(dataFinal.getTime());
		BigDecimal value = BigDecimal.ZERO;
		try {
			Connection con = ConectaMySql.obtemConexao();
			PreparedStatement statement = con
					.prepareStatement("SELECT  sum(i.valor_custo * i.quantidade_estoque) FROM produto i WHERE i.data_criacao_prod BETWEEN  ?  and  ? ");
			statement.setString(1, cFirstString);
			statement.setString(2, cEndString);
			ResultSet result = statement.executeQuery();
			result.next();
			String sum = result.getString(1);
			System.out.println(sum);
			value = new BigDecimal(sum);
			con.close();
		} catch (Exception exc) {
			System.out.println(exc.getMessage());
		}
		return value;
	}

	public BigDecimal totalVendasRestric() {
		SimpleDateFormat dataFormadata = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.S");
		Calendar dataInicial = Calendar.getInstance();
		Calendar dataFinal = Calendar.getInstance();
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataInicial.add(Calendar.DAY_OF_MONTH, -30);
		String cFirstString = dataFormadata.format(dataInicial.getTime());
		String cEndString = dataFormadata.format(dataFinal.getTime());
		BigDecimal value = BigDecimal.ZERO;
		try {
			Connection con = ConectaMySql.obtemConexao();
			PreparedStatement statement = con
					.prepareStatement("SELECT SUM(valor_total) FROM pedido WHERE data_criacao between ? and ? AND forma_pagamento = 'DINHEIRO' AND status = 'EMITIDO' ");
			statement.setString(1, cFirstString);
			statement.setString(2, cEndString);
			ResultSet result = statement.executeQuery();
			result.next();
			String sum = result.getString(1);
			System.out.println(sum);
			value = new BigDecimal(sum);
			con.close();
		} catch (Exception exc) {
			System.out.println(exc.getMessage());
		}
		return value;
	}
	
	
	public BigDecimal totalParcRestricPago() {
		SimpleDateFormat dataFormadata = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.S");
		Calendar dataInicial = Calendar.getInstance();
		Calendar dataFinal = Calendar.getInstance();
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataInicial.add(Calendar.DAY_OF_MONTH, -30);
		String cFirstString = dataFormadata.format(dataInicial.getTime());
		String cEndString = dataFormadata.format(dataFinal.getTime());
		BigDecimal value = BigDecimal.ZERO;
		try {
			Connection con = ConectaMySql.obtemConexao();
			PreparedStatement statement = con
					.prepareStatement("SELECT SUM(valores) FROM parcelamento WHERE data between ? and ? and pagamento = 'PAGA' ");
			statement.setString(1, cFirstString);
			statement.setString(2, cEndString);
			ResultSet result = statement.executeQuery();
			result.next();
			String sum = result.getString(1);
			System.out.println(sum);
			value = new BigDecimal(sum);
			con.close();
		} catch (Exception exc) {
			System.out.println(exc.getMessage());
		}
		return value;
	}
	
	public BigDecimal totalParcRestricQuitado() {
		SimpleDateFormat dataFormadata = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.S");
		Calendar dataInicial = Calendar.getInstance();
		Calendar dataFinal = Calendar.getInstance();
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataInicial.add(Calendar.DAY_OF_MONTH, -30);
		String cFirstString = dataFormadata.format(dataInicial.getTime());
		String cEndString = dataFormadata.format(dataFinal.getTime());
		BigDecimal value = BigDecimal.ZERO;
		try {
			Connection con = ConectaMySql.obtemConexao();
			PreparedStatement statement = con
					.prepareStatement("SELECT SUM(parc.valores) FROM parcelamento parc INNER JOIN pedido p ON p.id = parc.pedido_id WHERE pagamento = 'QUITADO' AND p.data between ? and ? ");
			statement.setString(1, cFirstString);
			statement.setString(2, cEndString);
			ResultSet result = statement.executeQuery();
			result.next();
			String sum = result.getString(1);
			System.out.println(sum);
			value = new BigDecimal(sum);
			con.close();
		} catch (Exception exc) {
			System.out.println(exc.getMessage());
		}
		return value;
	}
	
	
	
	BigDecimal totalVendaRestric = this.totalVendasRestric().add(this.totalParcRestricPago()).add(this.totalParcRestricQuitado());
	BigDecimal totalCusto = this.leftEstoque().add(this.inEstoque());
	BigDecimal totalVenda = this.totalVendas();

	private CartesianChartModel categoryModel;

	public GraficoFluxoCaixaBean() {
		categoryModel = new CartesianChartModel();

		ChartSeries boys = new ChartSeries();
		boys.setLabel("Custo");
		boys.set("2015", totalCusto);

		ChartSeries girls = new ChartSeries();
		girls.setLabel("Total Vendas");
		girls.set("2015", totalVenda);

		ChartSeries teste = new ChartSeries();
		teste.setLabel("Total Vendas Recebidas");
		teste.set("2015", totalVendaRestric);

		categoryModel.addSeries(teste);
		categoryModel.addSeries(boys);
		categoryModel.addSeries(girls);
	}

	public CartesianChartModel getCategoryModel() {
		return categoryModel;
	}

	public void setCategoryModel(CartesianChartModel categoryModel) {
		this.categoryModel = categoryModel;
	}
}
