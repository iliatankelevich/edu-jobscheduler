package edu.ilia.jobsystem.service.executers;


import edu.ilia.jobssystem.models.Job;
import edu.ilia.jobssystem.models.JobServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author ilia.tankelevich
 * @date 21/02/2020
 */
public interface JobExecutor {
    JobServiceResponse execute(Job job);
    default String getName(){
        String name;
        Component componentAnnotation = this.getClass().getAnnotation(Component.class);
        if(componentAnnotation != null && !componentAnnotation.value().isEmpty()){
            name = componentAnnotation.value();
        }else{
            name = this.getClass().getSimpleName().replace(this.getClass().getSuperclass().getSimpleName(),"");
        }

        return name;
    }

    default void cleanup(){
        LoggerFactory.getLogger(this.getClass()).info("no cleanup configured for {}", this.getClass().getSimpleName());
    }
}
