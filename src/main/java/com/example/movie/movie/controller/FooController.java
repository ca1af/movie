package com.example.movie.movie.controller;

import com.example.movie.movie.service.FooService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FooController {
    private final FooService fooService;

    @GetMapping("/foo/{id}")
    public Long getCount(@PathVariable Long id){
        return fooService.getFoo(id).getCount();
    }
    @PostMapping("/foo")
    public Long saveFoo(){
        return fooService.saveFoo();
    }

    @PostMapping("/foo/dirty")
    public void addCountDirty(@RequestParam Long id){
        fooService.addCountByDirtyCheck(id);
    }

    @PostMapping("/foo/query")
    public void addCountQuery(@RequestParam Long id){
        fooService.addCountQuery(id);
    }

    @PostMapping("/foo/exclusive")
    public void addCountExclusiveLock(@RequestParam Long id){
        fooService.addCountForUpdate(id);
    }
}
