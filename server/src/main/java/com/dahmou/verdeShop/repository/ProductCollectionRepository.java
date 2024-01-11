package com.dahmou.verdeShop.repository;

import com.dahmou.verdeShop.model.Gender;
import com.dahmou.verdeShop.model.Product;
import com.dahmou.verdeShop.model.Size;
import com.dahmou.verdeShop.model.Type;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductCollectionRepository {

    private final JdbcTemplate jdbcTemplate;


    public ProductCollectionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private static Product mapRow(ResultSet rs, int rowNum) throws SQLException {

        String[] highlights = (String[]) rs.getArray("highlights").getArray();
        String[] imageSrc = (String[]) rs.getArray("image_src").getArray();
        String[] colors = (String[]) rs.getArray("colors").getArray();
        String[] options = (String[]) rs.getArray("options").getArray();
        Size[] size = (Size[]) rs.getArray("size").getArray();

        return new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("price"),
                rs.getString("description"),
                highlights,
                rs.getString("details"),
                imageSrc,
                rs.getString("image_alt"),
                colors,
                Gender.valueOf(rs.getString("gender")),
                size,
                options,
                Type.valueOf(rs.getString("type"))
        );


    }

    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM Product";

        return jdbcTemplate.query(sql, ProductCollectionRepository::mapRow);
    }

    public void createProduct(Product product) {
        String sql = "INSERT INTO Product (name, price, description, highlights, details, image_src, image_alt, colors, gender, sizes, options, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, product.name(), product.price(), product.description(), product.highlights(), product.details(), product.imageSrc(), product.imageAlt(), product.colors(), product.gender().toString(), product.sizes().toString(), product.options(), product.type().toString());
    }
    public void updateProduct(int id,Product product) {
        String sql = "UPDATE Product SET name=?, price=?, description=?, highlights=?, details=?, image_src=?, image_alt=?, colors=?, gender=?, sizes=?, options=?, type=? WHERE id=?";
        jdbcTemplate.update(sql, product.name(), product.price(), product.description(), product.highlights(), product.details(), product.imageSrc(), product.imageAlt(), product.colors(), product.gender().toString(), product.sizes().toString(), product.options(), product.type().toString(), id);
    }

    public void deleteProduct(int id){
        String sql = "DELETE FROM Product WHERE id=?";
        jdbcTemplate.update(sql, id);

    }

    public Product getProduct(int id){
        try {
            String sql = "SELECT * FROM Product WHERE id=?";
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, ProductCollectionRepository::mapRow);

        }catch (EmptyResultDataAccessException e) {
            // Handle empty result - throw a custom exception or return null
            return null; // Or throw a custom exception
        }
    }

}


