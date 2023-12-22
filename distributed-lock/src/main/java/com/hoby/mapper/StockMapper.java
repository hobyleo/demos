package com.hoby.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hoby.entity.Stock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author hoby
 * @since 2023-02-21
 */
public interface StockMapper extends BaseMapper<Stock> {

    @Update("update db_stock set count = count - #{count} where id = #{id} and count >= #{count}")
    int deductStock(@Param("id") Long id, @Param("count") int count);

    @Select("select * from db_stock where product_code = #{productCode} for update")
    List<Stock> queryStock(@Param("productCode") String productCode);

}
