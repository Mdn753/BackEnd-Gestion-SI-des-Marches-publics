package com.DPETL.DPETL.service.interfac;

import com.DPETL.DPETL.DTO.Response;

public interface IEvaluationService {

    Response EvaluteOffre(Integer Offreid,boolean iswinning);
    Response GetWinningOffre(Integer id);
}
