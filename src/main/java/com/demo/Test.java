package com.demo;

import com.demo.dao.TestscoreMapper;
import com.demo.model.Testscore;
import com.demo.model.TestscoreExample;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.util.List;

public class Test {
   static Logger logger=Logger.getLogger(Test.class);
    public static void main(String[] args) {
        selectUser();
    }
    public static void selectUser(){
        SqlSession sqlSession = MysqlUtil.getSqlSession();
        TestscoreMapper mapper = sqlSession.getMapper(TestscoreMapper.class);
        TestscoreExample example= new TestscoreExample();
//        example.setDistinct(true);
//        example.setOrderByClause("score desc limit 1");
        TestscoreExample.Criteria criteria= example.createCriteria();
        criteria.andScoreGreaterThanOrEqualTo(10);
   /*    int i= mapper.deleteByExample(example);
       System.out.println(i);
        sqlSession.commit();*/
       List<Testscore> list = mapper.selectByExample(example);
        for(Testscore t:list){
            logger.info("学生id:"+t.getStudentId());
            System.out.println(t.getScore());
        }

    }
}
