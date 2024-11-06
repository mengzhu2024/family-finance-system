package com.xust.ffms.service.impl;

import com.xust.ffms.dao.DebtMapper;
import com.xust.ffms.entity.Debt;
import com.xust.ffms.service.DebtService;
import com.xust.ffms.utils.PageModel;
import com.xust.ffms.utils.Result;
import com.xust.ffms.utils.ResultUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DebtServiceImpl implements DebtService {
    @Resource
    DebtMapper mapper;
    @Override
    public Result<Debt> findByWhere(PageModel model) {

        List<Debt> bills = mapper.findByWhere(model);
        if (bills.size()>=0){
            Result<Debt> result = ResultUtil.success(bills);
            result.setTotal(mapper.getTotalByWhere(model));
            if (result.getTotal() == 0) {
                result.setMsg("没有查到相关数据");
            } else {
                result.setMsg("数据获取成功");
            }
            return result;
        }else {
            return ResultUtil.unSuccess("获取数据失败！");
        }
    }

    @Override
    public int add(Debt debt) {
        return mapper.add(debt);
    }

    @Override
    public int update(Debt debt) {
        return mapper.update(debt);
    }

    @Override
    public int del(int id) {
        return mapper.del(id);
    }

    @Override
    public Result<Debt> findByWhereNoPage(Debt debt) {
        List<Debt> bills = mapper.findByWhereNoPage(debt);
        Result<Debt> result = ResultUtil.success(bills);
        result.setMsg("数据获取成功");
        return result;
    }

}
