<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:math="http://www.w3.org/2005/xpath-functions/math"
  xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl"
  xmlns:gc="http://efl.fr/chaine/saxon-pipe/config"
  xmlns:saxon="http://saxon.sf.net/"
  exclude-result-prefixes="#all"
  version="3.0">

  <xd:doc scope="stylesheet">
    <xd:desc>
      <xd:p>Permet de tranformer le fichier de config gaulois-pipe des revues de la chaine XML au format du généric importer</xd:p>
      <xd:p></xd:p>
    </xd:desc>
  </xd:doc>
  
  <xsl:output saxon:indent-spaces="2" indent="yes"/>
  
  <xd:doc>
    <xd:desc>Une simple recopie</xd:desc>
  </xd:doc>
  <xsl:template match="@* | node()">
    <xsl:copy>
      <xsl:apply-templates select="@* | node()"/>
    </xsl:copy>
  </xsl:template>
  
  <xd:doc>
    <xd:desc>Ré-écriture des paramètres, pour ajouter destDir et sourceDir qui sont requis par le générique importeur</xd:desc>
  </xd:doc>
  <xsl:template match="gc:params">
    <xsl:copy xmlns="http://efl.fr/chaine/saxon-pipe/config">
      <xsl:apply-templates select="@* | node() except (gc:param[@name=('destDir','sourceDir')])"/>
      <param name="destDir" abstract="true"/>
      <param name="sourceDir" abstract="true"/>
    </xsl:copy>
  </xsl:template>

  <xd:doc>
    <xd:desc>On ré-écrit les sources pour être conforme au generic importer</xd:desc>
  </xd:doc>
  <xsl:template match="gc:sources">
    <sources orderBy="size" sort="desc" xmlns="http://efl.fr/chaine/saxon-pipe/config">
      <folder href="$[sourceDir]" pattern="\p{{ASCII}}+.[xml|XML]$" recurse="true"/>
    </sources>
  </xsl:template>
  
  <xd:doc>
    <xd:desc>On enlève le indent</xd:desc>
  </xd:doc>
  <xsl:template match="gc:output">
    <xsl:copy>
      <xsl:apply-templates select="@* except @indent | node()"/>
    </xsl:copy>
  </xsl:template>
  
  <xd:doc>
    <xd:desc>On met le output dans le bon dossier, conformément au generic importer</xd:desc>
  </xd:doc>
  <xsl:template match="gc:output/gc:folder">
    <xsl:copy>
      <xsl:attribute name="href">$[sourceDir]</xsl:attribute>
      <xsl:apply-templates select="@* except @href"/>
    </xsl:copy>
  </xsl:template>

  <xd:doc>
    <xd:desc>Ré-écriture des paramètres inputDirPath et outputDirPath, nécessaires aux XSL, mais initialisés à partir de sourceDir et destDir</xd:desc>
  </xd:doc>  
  <xsl:template match="gc:params/gc:param[@name='inputDirPath']">
    <xsl:copy>
      <xsl:copy-of select="@name"/>
      <xsl:attribute name="value">$[sourceDir]</xsl:attribute>
    </xsl:copy>
  </xsl:template>
  <xsl:template match="gc:params/gc:param[@name='outputDirPath']">
    <xsl:copy>
      <xsl:copy-of select="@name"/>
      <xsl:attribute name="value">$[destDir]</xsl:attribute>
    </xsl:copy>
  </xsl:template>
  
</xsl:stylesheet>
