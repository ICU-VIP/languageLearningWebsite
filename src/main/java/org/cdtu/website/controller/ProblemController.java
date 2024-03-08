package org.cdtu.website.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.cdtu.website.entity.AtomicProblem;
import org.cdtu.website.entity.HttpResult;
import org.cdtu.website.entity.ProblemSet;
import org.cdtu.website.entity.table.AtomicProblemTableDef;
import org.cdtu.website.entity.table.UserTableDef;
import org.cdtu.website.service.AtomicProblemService;
import org.cdtu.website.service.ProblemSetService;
import org.cdtu.website.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/problem")
public class ProblemController {
    AtomicProblemService atomicProblemService;

    @Autowired
    public void setAtomicProblemService(AtomicProblemService atomicProblemService) {
        this.atomicProblemService = atomicProblemService;
    }

    ProblemSetService problemSetService;

    @Autowired
    public void setProblemSetService(ProblemSetService problemSetService) {
        this.problemSetService = problemSetService;
    }

    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/saveAtomicProblem")
    public HttpResult<Object> saveProblem(@RequestBody AtomicProblem problem) {
        atomicProblemService.save(problem);
        return HttpResult.success(null);
    }

    @DeleteMapping("/deleteAtomicProblem")
    public HttpResult<Object> deleteProblem(@RequestParam Long id) {
        atomicProblemService.removeById(id);
        return HttpResult.success(null);
    }

    @PutMapping("/updateAtomicProblem")
    public HttpResult<Object> updateProblem(@RequestBody AtomicProblem problem) {
        atomicProblemService.updateById(problem);
        return HttpResult.success(null);
    }

    @GetMapping("/getAllAtomicProblemBySetId")
    public HttpResult<Object> getAllAtomicProblemBySetId(@RequestParam Integer setId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select()
                .where(AtomicProblemTableDef.ATOMIC_PROBLEM.SET_ID.eq(setId));
        List<AtomicProblem> list = atomicProblemService.list(queryWrapper);
        return HttpResult.success(list);
    }

    @PostMapping("/saveProblemSet")
    public HttpResult<Object> saveProblemSet(@RequestBody ProblemSet problemSet) {
        problemSet.setCreateBy(userService.getCurrentUserId());
        problemSetService.saveProblemSetAndAtomic(problemSet);
        return HttpResult.success(null);
    }

    @DeleteMapping("/deleteProblemSet")
    public HttpResult<Object> deleteProblemSet(@RequestParam Long id) {
        problemSetService.removeById(id);
        return HttpResult.success(null);
    }

    @PutMapping("/updateProblemSet")
    public HttpResult<Object> updateProblemSet(@RequestBody ProblemSet problemSet) {
        problemSet.setUpdateBy(userService.getCurrentUserId());
        problemSetService.updateProblemSetAndAtomic(problemSet);
        return HttpResult.success(null);
    }


    @GetMapping("/getProblemSet")
    public HttpResult<Object> getProblemSet(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        Page<ProblemSet> page = Page.of(pageNum, pageSize);
        problemSetService.page(page);
        QueryWrapper queryWrapper = null;
        // 添加创建者信息
        for (ProblemSet problemSet : page.getRecords()) {
            queryWrapper = QueryWrapper.create()
                    .select(UserTableDef.USER.USERNAME)
                    .where(UserTableDef.USER.ID.eq(problemSet.getCreateBy()));
            problemSet.setCreatorName(userService.getOneAs(queryWrapper, String.class));
        }

        return HttpResult.success(page);
    }

    @GetMapping("/getProblemSetById")
    public HttpResult<Object> getProblemSet(@RequestParam Long id) {
        ProblemSet problemSet = problemSetService.getById(id);
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select()
                .where(AtomicProblemTableDef.ATOMIC_PROBLEM.SET_ID.eq(id));
        problemSet.setAtomicProblems(atomicProblemService.list(queryWrapper));
        return HttpResult.success(problemSet);
    }
}

