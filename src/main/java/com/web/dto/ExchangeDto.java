package com.web.dto;

import com.web.entity.Exchange;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ExchangeDto {

    private Exchange exchange;

    private List<String> linkImage = new ArrayList<>();
}
