package com.demo;

import com.demo.Thread.Mythread1;
import com.demo.Thread.Mythread2;
import com.demo.Util.Resource;
import com.demo.Util.Timer;
import com.demo.dao.CompressMapper;
import com.demo.dao.DetailsMapper;
import com.demo.dao.SignalsMapper;
import com.demo.dao.StandardMapper;
import com.demo.model.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Fight {
    static ExecutorService fixedThreadPoo1 = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    //查信号
    public static List<Signals> serachSignals() throws InvocationTargetException, IllegalAccessException {
        SqlSession sqlSession = MysqlUtil.getSqlSession();
        Endstatus endstatus = new Endstatus();
        SignalsMapper mapper = sqlSession.getMapper(SignalsMapper.class);
        SignalsExample example = new SignalsExample();
        SignalsExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(0);
        criteria.andStyleEqualTo("掉期");
        List<Signals> list = mapper.selectByExample(example);
        sqlSession.close();
        return list;
    }

    //处理信号

    public static List<Signals> doSignals(List<Signals> list) throws InvocationTargetException, IllegalAccessException {
        List<Signals> list1 = new ArrayList<>();
        Signals signals1 = new Signals(); //掉期刷新临时变量
        Signals signals2 = new Signals(); //期权刷新临时变量
        Signals signals3 = new Signals(); //人工刷新临时变量
        for (int i = 0; i < list.size(); i++) {
            Signals signals = list.get(i);
            if ("阈值保存".equals(signals.getInformation().trim())) {
                list1.add(signals);
                continue;
            }
            if ("人工保存".equals(signals.getInformation().trim())) {
                list1.add(signals);
                continue;
            }
            if ("掉期刷新".equals(signals.getInformation().trim())) {
                if (signals1.getPrice() == null) {
                    BeanUtils.copyProperties(signals1, signals);
                } else {
                    if (signals1.getPrice().getTime() < signals.getPrice().getTime()) {
                        BeanUtils.copyProperties(signals1, signals);
                    }
                }
            }
            if ("期权刷新".equals(signals.getInformation().trim())) {
                if (signals2.getPrice() == null) {
                    BeanUtils.copyProperties(signals2, signals);
                } else {
                    if (signals2.getPrice().getTime() < signals.getPrice().getTime()) {
                        BeanUtils.copyProperties(signals2, signals);
                    }
                }
                continue;
            }
            if ("人工刷新".equals(signals.getInformation().trim())) {
                if (signals3.getPrice() == null) {
                    BeanUtils.copyProperties(signals3, signals);
                } else {
                    if (signals3.getPrice().getTime() < signals.getPrice().getTime()) {
                        BeanUtils.copyProperties(signals3, signals);
                    }
                }
                continue;
            }
        }

        if (signals1 != null && signals2 != null) {
            if (signals1.getPrice().getTime() == signals2.getPrice().getTime()) {
                list1.add(signals1);
            } else {
                list1.add(signals1);
                list1.add(signals2);
            }
        } else if (signals1 != null && signals2 == null) {
            list1.add(signals1);
        } else if (signals1 == null && signals2 != null) {
            list1.add(signals2);
        } else if (signals1 == null && signals2 == null && signals3 != null) {
            list1.add(signals3);
        }
        return list1;
    }

    //处理新增交易
    public static Map<String, Object> Handle() {
        SqlSession sqlSession = MysqlUtil.getSqlSession();

        try {
            Details details1 = new Details();
            details1.setIscompress(1);
            Date date = null;
            List<String> stringList = new ArrayList<>();
            DetailsMapper mapper = sqlSession.getMapper(DetailsMapper.class);
            DetailsExample example = new DetailsExample();
            DetailsExample.Criteria criteria = example.createCriteria();
            criteria.andIscompressEqualTo(0);
            List<Details> list = mapper.selectByExample(example);//新增交易列表
            for (int i = 0; i < list.size(); i++) {
                Details details = list.get(i);
                if (date == null) {
                    date = details.getTrans();
                } else {
                    if (date.getTime() < details.getTrans().getTime()) {
                        date = details.getTrans();

                    }
                }
                String temp = details.getCurrency() + "|" + simpleDateFormat.format(details.getTransdate());
                stringList.add(temp);
                details1.setRowId(details.getRowId());
                mapper.updateByPrimaryKeySelective(details1);
                // sqlSession.commit();

            }
            String stringDate = simpleDateFormat1.format(date);
            Map map = new HashMap();
            map.put("date", stringDate);
            map.put("list", stringList);
            return map;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            sqlSession.close();
        }
    }


    //查询有无新增交易
    public static Boolean query() {
        SqlSession sqlSession = MysqlUtil.getSqlSession();

        try {

            DetailsMapper mapper = sqlSession.getMapper(DetailsMapper.class);
            DetailsExample example = new DetailsExample();
            DetailsExample.Criteria criteria = example.createCriteria();
            criteria.andIscompressEqualTo(0);
            long count = mapper.countByExample(example);
            if (count == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        } finally {
            sqlSession.close();
        }
    }

    //压缩交易 true 代表已压缩  false 代表没压缩
    public static List<Compress> compress(List<String> list) {
        SqlSession sqlSession = MysqlUtil.getSqlSession();
        List<Compress> list2 = new ArrayList<>();
        try {
            Set<String> set = new HashSet<>(list);
            list.clear();
            list.addAll(set);

            for (int i = 0; i < list.size(); i++) {
                Compress compress = new Compress();
                String string = list.get(i);
                String[] String = string.split("\\|");
                compress.setCurrency(String[0]);
                compress.setTransdate(simpleDateFormat.parse(String[1]));
                list2.add(compress);
            }
            CompressMapper mapper = sqlSession.getMapper(CompressMapper.class);
            CompressExample example = new CompressExample();
            List<Compress> list1 = mapper.selectByExample(example);
            list2.removeAll(list1);
            //插入新压缩的数据
            if (list2.size() > 0) {
                for (int i = 0; i < list2.size(); i++) {
                    int ii = mapper.insertSelective(list2.get(i));
                    if (ii > 0) {
                        System.out.println("插入成功");
                    } else {
                        System.out.println("插入失败");
                    }
                }
                sqlSession.commit();
            }
            return list2;
        } catch (Exception e) {
            System.out.println(e);
            return list2;
        } finally {
            sqlSession.close();
        }
    }

    //标准期限合并压缩表
    public static List<Compress> merge(List<Compress> list, boolean flag) {

        SqlSession sqlSession = MysqlUtil.getSqlSession();
        List<Compress> list1 = new ArrayList<>(); //返回数据
        List<Compress> list2 = new ArrayList<>(); //标准期限
        List<Compress> list3 = new ArrayList<>(); //压缩数据

        CompressMapper mapper = sqlSession.getMapper(CompressMapper.class);
        CompressExample example = new CompressExample();
        list3 = mapper.selectByExample(example);

        if (list == null) {
            list1 = list3;//原来压缩数据
        } else if (!flag) {
            list1 = list;//新增压缩数据
        } else {
            list.addAll(list3);
            list1 = list;
        }

        StandardMapper mapper1 = sqlSession.getMapper(StandardMapper.class);
        StandardExample example1 = new StandardExample();
        List<Standard> standardList = mapper1.selectByExample(example1);
        for (int i = 0; i < standardList.size(); i++) {
            Compress compress = new Compress();
            compress.setCurrency(standardList.get(i).getCurrency());
            compress.setTransdate(standardList.get(i).getTransdate());
            list2.add(compress);
        }
        list1.addAll(list2);
        Set<Compress> set = new HashSet<>(list1);
        list1.clear();
        list1.addAll(set);
        return list1;
    }

    //任务一查询
    public static List<Details> query1() {

        SqlSession sqlSession = MysqlUtil.getSqlSession();
        DetailsMapper mapper = sqlSession.getMapper(DetailsMapper.class);
        DetailsExample example = new DetailsExample();
        DetailsExample.Criteria criteria = example.createCriteria();
        criteria.andIscompressEqualTo(0);
        criteria.andIsfixedEqualTo(1);
        List<Details> list = mapper.selectByExample(example);
        return list;
    }


    //任务一
    public static void task1(Timer timer1) throws InterruptedException {
        Resource resource1 = new Resource(timer1.getCount(), new LinkedBlockingQueue<Details>(timer1.getNum()));
        resource1.setList(timer1.getList());
        for (int i = 0; i < timer1.getNum(); i++) {
            resource1.getResourceQueue().put(timer1.getList().get(i));
        }
        Mythread1 mythread1 = new Mythread1(resource1);

        fixedThreadPoo1.execute(mythread1);
        fixedThreadPoo1.execute(mythread1);
        fixedThreadPoo1.execute(mythread1);
    }

    //任务二
    public static void task2(Timer timer2) throws InterruptedException {
        Resource resource2 = new Resource(timer2.getCount(), new LinkedBlockingQueue<Details>(timer2.getNum()));
        resource2.setList(timer2.getList());
        for (int i = 0; i < timer2.getNum(); i++) {
            resource2.getResourceQueue().put(timer2.getList().get(i));
        }
        Mythread2 mythread2 = new Mythread2(resource2);

        fixedThreadPoo1.execute(mythread2);
        fixedThreadPoo1.execute(mythread2);
        fixedThreadPoo1.execute(mythread2);

    }

    //总任务
    public static void task(String time, List<Compress> list2) throws InterruptedException, ExecutionException {
        System.out.println(time);
        long start = System.currentTimeMillis();
        int num1;
        int num2;

        Timer timer1 = new Timer();
        Timer timer2 = new Timer();


        List<Details> list1 =query1();
        num1 = list1.size();
        timer1 = new Timer(num1, new AtomicInteger(num1), list1);

        if (list1.size() > 0) {
            task1(timer1);//任务一
        }

        num2 = list2==null?0:list2.size();
        //   timer2 = new Timer(num2, new AtomicInteger(num2 + 1), list2);
        timer2 = new Timer(num2, new AtomicInteger(num2), list2);

        if (list2.size() > 0) {
            task2(timer2);
        }//任务二

        while (true) {

            System.out.println(timer1.getCount().intValue() + "--" + timer2.getCount().intValue());
            if (timer1.getCount().intValue() == 0 && timer2.getCount().intValue() == 0) {
                long end = System.currentTimeMillis();

                System.out.println(System.currentTimeMillis() + "系统结束----->" + "耗时" + (end - start) + "ms");
                fixedThreadPoo1.shutdownNow();
                break;
            }

        }
    }


    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, ExecutionException, InterruptedException {

        List<Signals> list = serachSignals();
        if (list == null || list.size() == 0) {
            if (!query()) {
                System.out.println("没有新增交易，不需要重估");
            } else {
                Map<String, Object> map = Handle();
                List<Compress> list1 = new ArrayList<>();
                List<Compress> list2 = compress((List<String>) map.get("list"));
                if (list2.size() > 0) {
                    list1 = merge(list2, false);
                } else {
                    list1 = null;
                }
                task("价格",list1);
            }

        } else {
            List<Signals> signalsList = doSignals(list);
            Signals signals = signalsList.get(0);
            if (!query()) {//没有新增交易
                //用新价格+原来压缩表
                List<Compress> list1 =merge(null,false);
                task("价格", list1);

            } else {//有新增交易

                Map<String, Object> map = Handle();
                List<Compress> list1 = new ArrayList<>();
                List<Compress> list2 = compress((List<String>) map.get("list"));
                if (list2.size() > 0) {
                    list1 = merge(list2, true);
                } else {
                    list1 = merge(null, false);
                }
                task("价格", list1);


            }
        }
    }
}