/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpmlab.invio.rn.pdf;

import bpmlab.invio.entidade.Qualis;
import bpmlab.invio.entidade.QualisPK;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Junior
 */
public class PDFBoxRegexRN {

    private List<Qualis> osQualis;

    public List<Qualis> criar(String[] linhas) {
        ArrayList<Qualis> resposta = new ArrayList<Qualis>();

        boolean BEGIN = false;
        StringBuilder registro = new StringBuilder();


        Pattern patternIssn = Pattern.compile("(\\d\\d\\d\\d)-(\\d\\d\\d\\w\\s)");
        Pattern patternIssnErrado = Pattern.compile("ISSN");
        Pattern patternEstratoAB = Pattern.compile("\\s[A-B][1-5]\\s");
        Pattern patternEstratoC = Pattern.compile("\\sC\\s");
        Pattern patternStatus = Pattern.compile("\\s(Em )?Atualiza[çd][ão][o]?");
        Pattern patternCessou = Pattern.compile("Cessou");

        Matcher issnMatcher;
        Matcher estratoABMatcher;
        Matcher estratoCMatcher;
        Matcher statusMatcher;

        Matcher issnMatcherResgistro;
        Matcher issnErradoMatcherResgistro;
        Matcher issnErradoMatcherLinha;
        Matcher estratoABMatcherResgistro;
        Matcher estratoCMatcherResgistro;
        Matcher statusMatcherResgistro;
        Matcher cessouMatcherResgistro;
        Matcher cessouMatcherLinha;

        int fimIssn = 0;
        int inicioEstrato = 0;
        int fimEstrato = 0;
        int inicioStatus;

        boolean issnExiste;
        boolean estratoABExiste;
        boolean estratoCExiste;
        boolean statusExiste;
        boolean issnErradoRegistroExiste;
        boolean issnErradoLinhaExiste;
        boolean cessouRegistroExiste;
        boolean cessouLinhaExiste;
        boolean issnResgistroExiste;
        boolean estratoABResgistroExiste;
        boolean estratoCResgistroExiste;

        String issn = null;
        String titulo = null;
        String estrato = null;
        String area = null;
        String status = null;

        Qualis qualis;
        QualisPK qualisPK;

        for (String linha : linhas) {

            cessouMatcherLinha = patternCessou.matcher(linha);
            issnErradoMatcherLinha = patternIssnErrado.matcher(linha);

            if (cessouMatcherLinha.find()) {
                cessouLinhaExiste = true;
            } else {
                cessouLinhaExiste = false;
            }
            if (issnErradoMatcherLinha.find()) {
                issnErradoLinhaExiste = true;
            } else {
                issnErradoLinhaExiste = false;
            }

            if (!cessouLinhaExiste && !issnErradoLinhaExiste) {

                if (BEGIN == false) { //Sempre iniciará de primeira Begin é falso.

                    //Pesquisando os patterns nas LINHAS e adicionando a variavel.
                    issnMatcher = patternIssn.matcher(linha);
                    if (issnMatcher.find()) {
                        issnExiste = true;
                    } else {
                        issnExiste = false;
                    }

                    estratoABMatcher = patternEstratoAB.matcher(linha);
                    if (estratoABMatcher.find()) {
                        estratoABExiste = true;
                    } else {
                        estratoABExiste = false;
                    }
                    estratoCMatcher = patternEstratoC.matcher(linha);
                    if (estratoCMatcher.find()) {
                        estratoCExiste = true;
                    } else {
                        estratoCExiste = false;
                    }
                    if (issnExiste) {
                        BEGIN = true;
                    } else if (estratoABExiste || estratoCExiste) {
                        BEGIN = true;
                    }
                }

                if (BEGIN == true) {

                    //A partir daqui já achei ISSN ou estrato e pequiso o padrão final (Status) da minha linha.
                    statusMatcher = patternStatus.matcher(linha);

                    registro.append(linha);
                    registro.append(" ");

                    if (statusMatcher.find()) {


                        //Addd tudo ao registro.
                        issnMatcherResgistro = patternIssn.matcher(registro);
                        if (issnMatcherResgistro.find()) {
                            issnResgistroExiste = true;
                        } else {
                            issnResgistroExiste = false;
                        }
                        issnErradoMatcherResgistro = patternIssnErrado.matcher(registro);
                        if (issnErradoMatcherResgistro.find()) {
                            issnErradoRegistroExiste = true;
                        } else {
                            issnErradoRegistroExiste = false;
                        }
                        estratoABMatcherResgistro = patternEstratoAB.matcher(registro);
                        if (estratoABMatcherResgistro.find()) {
                            estratoABResgistroExiste = true;
                        } else {
                            estratoABResgistroExiste = false;
                        }
                        estratoCMatcherResgistro = patternEstratoC.matcher(registro);
                        if (estratoCMatcherResgistro.find()) {
                            estratoCResgistroExiste = true;
                        } else {
                            estratoCResgistroExiste = false;
                        }
                        statusMatcherResgistro = patternStatus.matcher(registro);
                        statusMatcherResgistro.find();
                        cessouMatcherResgistro = patternCessou.matcher(registro);
                        if (cessouMatcherResgistro.find()) {
                            cessouRegistroExiste = true;
                        } else {
                            cessouRegistroExiste = false;
                        }
                        status = statusMatcherResgistro.group();

                        if (!cessouRegistroExiste || !issnErradoRegistroExiste) {

                            if (issnResgistroExiste) {

                                issn = issnMatcherResgistro.group();

                                if (estratoABResgistroExiste) {
                                    estrato = estratoABMatcherResgistro.group();
                                    try {
                                        titulo = registro.substring(issnMatcherResgistro.end(), estratoABMatcherResgistro.start());
                                        area = linha.substring(estratoABMatcherResgistro.end(), statusMatcherResgistro.start());
                                    } catch (java.lang.StringIndexOutOfBoundsException e) {
                                        System.out.println("Ocorreu um erro ao tentar obter por substring \nERRO: " + e);
                                        System.out.println("Titulo - " + titulo);
                                        System.out.println("Area - " + area);
                                    }

                                } else if (estratoCResgistroExiste) {

                                    estrato = estratoCMatcherResgistro.group();
                                    try {
                                        titulo = registro.substring(issnMatcherResgistro.end(), estratoCMatcherResgistro.start());
                                        area = linha.substring(estratoCMatcherResgistro.end(), statusMatcherResgistro.start());
                                    } catch (java.lang.StringIndexOutOfBoundsException e) {
                                        System.out.println("Ocorreu um erro ao tentar obter por substring \nERRO: " + e);
                                        System.out.println("Titulo - " + titulo);
                                        System.out.println("Area - " + area);
                                    }
                                }
                            } else if (estratoABResgistroExiste == true || estratoCResgistroExiste == true) {
                                issn = "";
                                if (estratoABResgistroExiste) {

                                    try {
                                        titulo = registro.substring(0, estratoABMatcherResgistro.start());
                                        area = linha.substring(estratoABMatcherResgistro.end(), statusMatcherResgistro.start());
                                    } catch (java.lang.StringIndexOutOfBoundsException e) {
                                        System.out.println("Ocorreu um erro ao tentar obter por substring \nERRO: " + e);
                                        System.out.println("Titulo - " + titulo);
                                        System.out.println("Area - " + area);
                                    }
                                } else if (estratoCResgistroExiste) {
                                    try {
                                        titulo = registro.substring(0, estratoCMatcherResgistro.start());
                                        area = linha.substring(estratoCMatcherResgistro.end(), statusMatcherResgistro.start());
                                    } catch (java.lang.StringIndexOutOfBoundsException e) {
                                        System.out.println("Ocorreu um erro ao tentar obter por substring \nERRO: " + e);
                                        System.out.println("Titulo - " + titulo);
                                        System.out.println("Area - " + area);
                                    }
                                }

                            }

                        } else {
                            System.out.println("Registro ignorado pois Cessou estava presente, registro: " + registro);
                        }
                    }

                    // Criar um novo objeto Qualis
                    BEGIN = false;
                    qualis = new Qualis(titulo, area, issn);
                    qualisPK = new QualisPK(titulo, area, issn);
                    qualis.setQualisPK(qualisPK);
                    qualis.setEstrato(estrato);
                    qualis.setStatus(status);
                        
                    resposta.add(qualis);    
                    
//                    System.out.println("Registro Tratado: " + registro);
                    registro = new StringBuilder();

                }
            }
        }

        return resposta;
    }
    
    public static void main(String[] args) {
        
    }
}
