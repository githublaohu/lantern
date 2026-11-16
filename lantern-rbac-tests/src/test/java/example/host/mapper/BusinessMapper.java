package example.host.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BusinessMapper {

    @Select("SELECT 1")
    int ping();
}
