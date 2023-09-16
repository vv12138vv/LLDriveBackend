package com.lldrive.service;

import com.lldrive.domain.entity.Repo;
import com.lldrive.domain.resp.CommonResp;

public interface RepoService {
    CommonResp createRepo(String userId, String repoId);
}
