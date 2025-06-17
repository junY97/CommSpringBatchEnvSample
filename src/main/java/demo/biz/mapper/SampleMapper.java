package demo.biz.mapper;

import demo.biz.dto.SampleDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author junyeong.jo .
 * @since 2025-06-17
 */
@Mapper
public interface SampleMapper {
    List<SampleDto> selectDummy();
    void insertHardcodedDummyData();
}
