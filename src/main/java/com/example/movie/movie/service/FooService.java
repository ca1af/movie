package com.example.movie.movie.service;

import com.example.movie.movie.entity.Foo;
import com.example.movie.movie.repository.FooRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FooService {
    private final FooRepository fooRepository;
    @Transactional
    public Foo getFoo(Long id){
        return fooRepository.findById(id).orElseThrow();
    }
    @Transactional
    public Long saveFoo(){
        Foo foo = new Foo(0L);
        fooRepository.save(foo);
        return foo.getId();
    }
    @Transactional // 사실 필요없음
    public void addCountQuery(Long id){
        fooRepository.addCount(id);
    }
    @Transactional
    public void addCountByDirtyCheck(Long id){
        Foo foo = fooRepository.findById(id).orElseThrow();
        foo.addCount();
    }

    @Transactional
    public void addCountForUpdate(Long id){
        Foo exclusiveFoo = fooRepository.getFooForUpdate(id);
        exclusiveFoo.addCount();
    }
}
