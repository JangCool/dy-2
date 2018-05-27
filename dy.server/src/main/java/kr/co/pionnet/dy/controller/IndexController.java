package kr.co.pionnet.dy.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.pionnet.dy.service.sample.SampleService;

@Controller
public class IndexController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Autowired
	private SampleService sampleService;
	
	@RequestMapping(value="/", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public @ResponseBody List index(Model model) throws Exception{
//		logger.debug("asdadadsa{}",sampleService.getDual());
		return sampleService.getDual();
		
	}
}
