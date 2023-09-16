package com.lldrive.service.impl;

import com.lldrive.domain.entity.Repo;
import com.lldrive.domain.resp.CommonResp;
import com.lldrive.domain.types.Status;
import com.lldrive.mapper.RepoMapper;
import com.lldrive.service.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepoServiceImpl implements RepoService {
    @Autowired
    RepoMapper repoMapper;

    @Override
    public CommonResp createRepo(String userId, String repoId) {
        Repo repo=new Repo();
        repo.setRepoId(repoId);
        repo.setUserId(userId);
        repo.setMaxCapacity(Long.valueOf(1073741824));//1g
        repo.setCurCapacity(Long.valueOf(0));
        int res=repoMapper.insert(repo);
        if(res==1){
            return new CommonResp(Status.SUCCESS,repo);
        }
        return new CommonResp(Status.SYSTEM_ERROR);
    }
}
