package kr.co.pionnet.dy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

@Configuration
public class TransactionAnnotationProcessor implements BeanPostProcessor{
	private ConfigurableListableBeanFactory configurableBeanFactory;
	
	@Autowired
    public TransactionAnnotationProcessor(ConfigurableListableBeanFactory beanFactory) {
        this.configurableBeanFactory = beanFactory;
    }
 
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    	System.out.println(beanName);
//        this.scanDataAccessAnnotation(bean, beanName);
        return bean;
    }
 
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) 
      throws BeansException {
        return bean;
    }
 
    protected void scanDataAccessAnnotation(Object bean, String beanName) {
        this.configureFieldInjection(bean);
    }
 
    private void configureFieldInjection(Object bean) {
        Class<?> managedBeanClass = bean.getClass();
        
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(managedBeanClass);
        System.out.println(managedBeanClass.getName());

        for (Method method : methods) {
            System.out.println(method.getName());

        	Transactional annotation = method.getAnnotation(Transactional.class);
        	if(annotation != null) {
        		try {
					Field  f = annotation.getClass().getField("value");
					
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
		}
        
//
//        FieldCallback fieldCallback = 
//          new DataAccessFieldCallback(configurableBeanFactory, bean);
//        ReflectionUtils.doWithFields(managedBeanClass, fieldCallback);
    }	
    
    public static void main(String[] args) throws Exception {
    	Class ano = TransactionalDynamic.class;
    	System.out.println(ano);
    	Method[] m = ano.getDeclaredMethods();
    	Field[] f = ano.getDeclaredFields();

    
//    	System.out.println(f.get(ano));
	}
}
