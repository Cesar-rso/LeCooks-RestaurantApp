package com.example.lecooks;

public class Produto {

	 private long id;
	  private String descricao;
	  private String Categoria;
	  private float preco;

	  public long getId() {
	    return id;
	  }

	  public void setId(long id) {
	    this.id = id;
	  }

	  public String getDescricao() {
	    return descricao;
	  }

	  public void setDescricao(String descricao) {
	    this.descricao = descricao;
	  }
	  
	  public String getCategoria() {
		    return Categoria;
	  }

	  public void setCategoria(String Categoria) {
		    this.Categoria = Categoria;
	  }
	  
	  public float getPreco() {
		  return preco;
	  }
	  
	  public void setPreco(float preco){
		  this.preco = preco;
	  }

	  // Usado pelo ArrayAdapter no ListView
	  @Override
	  public String toString() {
	    return descricao;
	  }
}
