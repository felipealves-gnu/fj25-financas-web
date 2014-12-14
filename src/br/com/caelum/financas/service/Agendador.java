package br.com.caelum.financas.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.*;

//@Singleton
@Stateless
public class Agendador {

    @Resource
    private TimerService timerService;

	private static int totalCriado;

//	@Schedule(second = "*", minute="*", hour = "*")
	public void executa() {
		System.out.printf("%d instancias criadas %n", totalCriado);

		// simulando demora de 4s na execucao
		try {
			System.out.printf("Executando %s %n", this);
			Thread.sleep(4000);
		} catch (InterruptedException e) {
		}
	}


    public void agenda(String expressaoMinutos, String expressaoSegundos){
        ScheduleExpression expression = new ScheduleExpression();
        expression.hour("*");
        expression.minute(expressaoMinutos);
        expression.second(expressaoSegundos);

        TimerConfig config = new TimerConfig();
        config.setInfo(expression.toString());
        config.setPersistent(false);

        this.timerService.createCalendarTimer(expression,config);

        System.out.println("Agendamento: "+ expression);
    }

    @Timeout
    public void verificacaoPeriodicaSeHaNovasContas(Timer timer){
        System.out.println(timer.getInfo());
    }

    @PostConstruct
    void posConstrucao(){
        System.out.printf("Criando agendador");
        totalCriado++;
    }
    @PreDestroy
    void preDestruicao(){
        System.out.printf("Destruindo agendador");
    }

}
