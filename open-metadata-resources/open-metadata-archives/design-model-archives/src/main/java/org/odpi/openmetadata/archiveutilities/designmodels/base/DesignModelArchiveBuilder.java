/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.designmodels.base;

import org.odpi.openmetadata.archiveutilities.designmodels.base.ffdc.DesignModelArchiveErrorCode;
import org.odpi.openmetadata.archiveutilities.designmodels.base.properties.ConceptModelDecoration;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveGUIDMap;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DesignModelArchiveBuilder creates the open metadata compliant instances for content
 * that is typically found in a common/standard design model.  This initial implementation
 * is focused on concept models.
 */
public class DesignModelArchiveBuilder
{
    private static final String  guidMapFileNamePostFix    = "GUIDMap.json";

    private static final String GLOSSARY_TYPE_NAME                       = "Glossary";
    private static final String EXTERNAL_GLOSSARY_LINK_TYPE_NAME         = "ExternalGlossaryLink";
    private static final String EXTERNALLY_SOURCED_GLOSSARY_TYPE_NAME    = "ExternallySourcedGlossary";
    private static final String CANONICAL_VOCABULARY_TYPE_NAME           = "CanonicalVocabulary";
    private static final String GLOSSARY_CATEGORY_TYPE_NAME              = "GlossaryCategory";
    private static final String SUBJECT_AREA_TYPE_NAME                   = "SubjectArea";
    private static final String CATEGORY_ANCHOR_TYPE_NAME                = "CategoryAnchor";
    private static final String CATEGORY_HIERARCHY_LINK_TYPE_NAME        = "CategoryHierarchyLink";
    private static final String GLOSSARY_TERM_TYPE_NAME                  = "GlossaryTerm";
    private static final String TERM_ANCHOR_TYPE_NAME                    = "TermAnchor";
    private static final String TERM_CATEGORIZATION_TYPE_NAME            = "TermCategorization";
    private static final String SEMANTIC_ASSIGNMENT_TYPE_NAME            = "TermAnchor";
    private static final String MORE_INFORMATION_TYPE_NAME               = "MoreInformation";
    private static final String DESIGN_MODEL_TYPE_NAME                   = "DesignModel";
    private static final String DESIGN_MODEL_GROUP_TYPE_NAME             = "DesignModelGroup";
    private static final String DESIGN_MODEL_ELEMENT_OWNERSHIP_TYPE_NAME = "DesignModelElementOwnership";
    private static final String DESIGN_MODEL_GROUP_OWNERSHIP_TYPE_NAME   = "DesignModelGroupOwnership";
    private static final String DESIGN_MODEL_GROUP_HIERARCHY_TYPE_NAME   = "DesignModelGroupHierarchy";
    private static final String DESIGN_MODEL_GROUP_MEMBERSHIP_TYPE_NAME  = "DesignModelGroupMembership";
    private static final String CONCEPT_BEAD_TYPE_NAME                   = "ConceptBead";
    private static final String CONCEPT_BEAD_LINK_TYPE_NAME              = "ConceptBeadLink";
    private static final String CONCEPT_BEAD_ATTRIBUTE_TYPE_NAME         = "ConceptBeadAttribute";
    private static final String CONCEPT_BEAD_ATTRIBUTE_LINK_TYPE_NAME    = "ConceptBeadAttributeLink";
    private static final String CONCEPT_BEAD_RELATIONSHIP_TYPE_NAME      = "ConceptBeadRelationshipEnd";

    private static final String QUALIFIED_NAME_PROPERTY  = "qualifiedName";
    private static final String DISPLAY_NAME_PROPERTY    = "displayName";
    private static final String TECHNICAL_NAME_PROPERTY  = "technicalName";
    private static final String DESCRIPTION_PROPERTY     = "description";
    private static final String LANGUAGE_PROPERTY        = "language";
    private static final String USAGE_PROPERTY           = "usage";
    private static final String SCOPE_PROPERTY           = "scope";
    private static final String URL_PROPERTY             = "url";
    private static final String ORGANIZATION_PROPERTY    = "organization";
    private static final String VERSION_PROPERTY         = "version";
    private static final String VERSION_NUMBER_PROPERTY  = "versionNumber";
    private static final String NAME_PROPERTY            = "name";
    private static final String STATUS_PROPERTY          = "status";
    private static final String CONFIDENCE_PROPERTY      = "confidence";
    private static final String AUTHOR_PROPERTY          = "author";
    private static final String ATTRIBUTE_NAME_PROPERTY  = "attribute";
    private static final String DECORATION_PROPERTY      = "decoration";
    private static final String POSITION_PROPERTY        = "position";
    private static final String MIN_CARDINALITY_PROPERTY = "minCardinality";
    private static final String MAX_CARDINALITY_PROPERTY = "maxCardinality";
    private static final String UNIQUE_VALUES_PROPERTY   = "uniqueValues";
    private static final String ORDERED_VALUES_PROPERTY  = "orderedValues";
    private static final String NAVIGABLE_PROPERTY       = "navigable";



    private OMRSArchiveBuilder archiveBuilder;
    private OMRSArchiveHelper  archiveHelper;
    private OMRSArchiveGUIDMap idToGUIDMap;

    private String             archiveRootName;
    private String             originatorName;
    private String             versionName;


    /**
     * Typical constructor passes parameters used to build the open metadata archive's property header.
     *
     * @param archiveGUID unique identifier for this open metadata archive.
     * @param archiveName name of the open metadata archive.
     * @param archiveDescription description of the open metadata archive.
     * @param archiveType enum describing the type of archive this is.
     * @param archiveRootName non-spaced root name of the open metadata archive elements.
     * @param originatorName name of the originator (person or organization) of the archive.
     * @param originatorLicense license for the content.
     * @param creationDate data that this archive was created.
     * @param versionNumber version number of the archive.
     * @param versionName version name for the archive.
     */
    protected DesignModelArchiveBuilder(String                     archiveGUID,
                                        String                     archiveName,
                                        String                     archiveDescription,
                                        OpenMetadataArchiveType    archiveType,
                                        String                     archiveRootName,
                                        String                     originatorName,
                                        String                     originatorLicense,
                                        Date                       creationDate,
                                        long                       versionNumber,
                                        String                     versionName)
    {
        List<OpenMetadataArchive>  dependentOpenMetadataArchives = new ArrayList<>();

        dependentOpenMetadataArchives.add(new OpenMetadataTypesArchive().getOpenMetadataArchive());

        this.archiveBuilder = new OMRSArchiveBuilder(archiveGUID,
                                                     archiveName,
                                                     archiveDescription,
                                                     archiveType,
                                                     originatorName,
                                                     originatorLicense,
                                                     creationDate,
                                                     dependentOpenMetadataArchives);

        this.archiveHelper = new OMRSArchiveHelper(archiveBuilder,
                                                   archiveGUID,
                                                   originatorName,
                                                   creationDate,
                                                   versionNumber,
                                                   versionName);

        this.idToGUIDMap = new OMRSArchiveGUIDMap(archiveRootName + guidMapFileNamePostFix);

        this.archiveRootName = archiveRootName;
        this.originatorName = originatorName;
        this.versionName = versionName;
    }


    /**
     * Create a glossary entity.  If the external link is specified, the glossary entity is linked to an
     * ExternalGlossaryLink entity.  If the scope is specified, the glossary entity is classified as
     * a CanonicalGlossary.
     *
     * @param qualifiedName unique name for the glossary
     * @param displayName display name for the glossary
     * @param description description about the glossary
     * @param language language that the glossary is written in
     * @param usage how the glossary should be used
     * @param externalLink link to material
     * @param scope scope of the content.
     *
     * @return id for the glossary
     */
    protected String addGlossary(String   qualifiedName,
                                 String   displayName,
                                 String   description,
                                 String   language,
                                 String   usage,
                                 String   externalLink,
                                 String   scope)
    {
        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(null, QUALIFIED_NAME_PROPERTY, qualifiedName);
        properties = archiveHelper.addStringPropertyToInstance(properties, DISPLAY_NAME_PROPERTY, displayName);
        properties = archiveHelper.addStringPropertyToInstance(properties, DESCRIPTION_PROPERTY, description);
        properties = archiveHelper.addStringPropertyToInstance(properties, LANGUAGE_PROPERTY, language);
        properties = archiveHelper.addStringPropertyToInstance(properties, USAGE_PROPERTY, usage);

        List<Classification> classifications = null;

        if (scope != null)
        {
            Classification  canonicalVocabClassification = archiveHelper.getClassification(CANONICAL_VOCABULARY_TYPE_NAME,
                                                                                           archiveHelper.addStringPropertyToInstance(null,
                                                                                                                                     SCOPE_PROPERTY,
                                                                                                                                     scope),
                                                                                           InstanceStatus.ACTIVE);

            classifications = new ArrayList<>();
            classifications.add(canonicalVocabClassification);
        }

        EntityDetail  glossaryEntity = archiveHelper.getEntityDetail(GLOSSARY_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(qualifiedName),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     classifications);

        archiveBuilder.addEntity(glossaryEntity);

        if (externalLink != null)
        {
            String externalLinkQualifiedName = qualifiedName + "_external_link";
            properties = archiveHelper.addStringPropertyToInstance(null, QUALIFIED_NAME_PROPERTY, externalLinkQualifiedName);
            properties = archiveHelper.addStringPropertyToInstance(properties, URL_PROPERTY, externalLink);
            properties = archiveHelper.addStringPropertyToInstance(properties, ORGANIZATION_PROPERTY, originatorName);
            properties = archiveHelper.addStringPropertyToInstance(properties, VERSION_PROPERTY, versionName);

            EntityDetail  externalLinkEntity = archiveHelper.getEntityDetail(EXTERNAL_GLOSSARY_LINK_TYPE_NAME,
                                                                             idToGUIDMap.getGUID(externalLinkQualifiedName),
                                                                             properties,
                                                                             InstanceStatus.ACTIVE,
                                                                             classifications);

            archiveBuilder.addEntity(externalLinkEntity);

            EntityProxy end1 = archiveHelper.getEntityProxy(glossaryEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(externalLinkEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(EXTERNALLY_SOURCED_GLOSSARY_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_link_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        return glossaryEntity.getGUID();
    }


    /**
     * Add a glossary category to the archive and connect it to glossary.
     *
     * @param glossaryId identifier of the glossary.
     * @param qualifiedName unique name for the category.
     * @param displayName display name for the category.
     * @param description description of the category.
     * @param subjectArea name of the subject area if this category contains terms for the subject area.
     *
     * @return identifier of the category
     */
    protected String addCategory(String   glossaryId,
                                 String   qualifiedName,
                                 String   displayName,
                                 String   description,
                                 String   subjectArea)
    {
        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(null, QUALIFIED_NAME_PROPERTY, qualifiedName);
        properties = archiveHelper.addStringPropertyToInstance(properties, DISPLAY_NAME_PROPERTY, displayName);
        properties = archiveHelper.addStringPropertyToInstance(properties, DESCRIPTION_PROPERTY, description);

        List<Classification> classifications = null;

        if (subjectArea != null)
        {
            Classification  subjectAreaClassification = archiveHelper.getClassification(SUBJECT_AREA_TYPE_NAME,
                                                                                        archiveHelper.addStringPropertyToInstance(null,
                                                                                                                                  NAME_PROPERTY,
                                                                                                                                  subjectArea),
                                                                                        InstanceStatus.ACTIVE);

            classifications = new ArrayList<>();
            classifications.add(subjectAreaClassification);
        }

        EntityDetail  categoryEntity = archiveHelper.getEntityDetail(GLOSSARY_CATEGORY_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(qualifiedName),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     classifications);

        archiveBuilder.addEntity(categoryEntity);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(glossaryId));
        EntityProxy end2 = archiveHelper.getEntityProxy(categoryEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(CATEGORY_ANCHOR_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(qualifiedName + "_anchor_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));

        return categoryEntity.getGUID();
    }


    /**
     * Add a term and link it to the glossary and an arbitrary number of categories.
     *
     * @param glossaryId unique identifier of the glossary
     * @param categoryIds unique identifiers of the categories
     * @param qualifiedName unique name of the term
     * @param displayName display name of the term
     * @param description description of the term
     *
     * @return unique identifier of the term
     */
    protected String addTerm(String       glossaryId,
                             List<String> categoryIds,
                             String       qualifiedName,
                             String       displayName,
                             String       description)
    {
        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(null, QUALIFIED_NAME_PROPERTY, qualifiedName);
        properties = archiveHelper.addStringPropertyToInstance(properties, DISPLAY_NAME_PROPERTY, displayName);
        properties = archiveHelper.addStringPropertyToInstance(properties, DESCRIPTION_PROPERTY, description);

        EntityDetail  termEntity = archiveHelper.getEntityDetail(GLOSSARY_TERM_TYPE_NAME,
                                                                 idToGUIDMap.getGUID(qualifiedName),
                                                                 properties,
                                                                 InstanceStatus.ACTIVE,
                                                                 null);

        archiveBuilder.addEntity(termEntity);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(glossaryId));
        EntityProxy end2 = archiveHelper.getEntityProxy(termEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(TERM_ANCHOR_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(qualifiedName + "_anchor_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));

        if (categoryIds != null)
        {
            for (String  categoryId : categoryIds)
            {
                if (categoryId != null)
                {
                    end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(categoryId));

                    archiveBuilder.addRelationship(archiveHelper.getRelationship(TERM_CATEGORIZATION_TYPE_NAME,
                                                                                 idToGUIDMap.getGUID(qualifiedName + "_category_" + categoryId + "_relationship"),
                                                                                 null,
                                                                                 InstanceStatus.ACTIVE,
                                                                                 end1,
                                                                                 end2));
                }
            }
        }

        return termEntity.getGUID();
    }


    /**
     * Link two categories together as part of the parent child hierarchy.
     *
     * @param parentCategoryId unique identifier for the parent category
     * @param childCategoryId unique identifier for the child category
     */
    protected void addCategoryToCategory(String  parentCategoryId,
                                         String  childCategoryId)
    {
        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(parentCategoryId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(childCategoryId));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(CATEGORY_HIERARCHY_LINK_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(parentCategoryId + "_to_" + childCategoryId),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create a navigation link from one referenceable to another to show they provide more information.
     *
     * @param describedElementId unique identifier for the element that is referencing the other.
     * @param describerElementId unique identifier for the element being pointed to.
     */
    protected void addMoreInformationLink(String  describedElementId,
                                          String  describerElementId)
    {
        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(describedElementId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(describerElementId));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(MORE_INFORMATION_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(describedElementId + "_to_" + describerElementId),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create a semantic assignment between a term and a Referenceable - for example a model element.
     *
     * @param termId identifier of term
     * @param referenceableId identifier of referenceable
     */
    protected void linkTermToReferenceable(String  termId,
                                           String  referenceableId)
    {
        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(referenceableId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(termId));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(SEMANTIC_ASSIGNMENT_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(referenceableId + "_to_" + termId),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create an element that represents a design model.
     *
     * @param qualifiedName unique name for the model
     * @param displayName display name
     * @param technicalName non-spaced name
     * @param description description
     * @param versionNumber version description
     * @param author author
     * @return guid of model
     */
    protected String addDesignModel(String   qualifiedName,
                                    String   displayName,
                                    String   technicalName,
                                    String   description,
                                    String   versionNumber,
                                    String   author)
    {
        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(null, QUALIFIED_NAME_PROPERTY, qualifiedName);
        properties = archiveHelper.addStringPropertyToInstance(properties, DISPLAY_NAME_PROPERTY, displayName);
        properties = archiveHelper.addStringPropertyToInstance(properties, TECHNICAL_NAME_PROPERTY, technicalName);
        properties = archiveHelper.addStringPropertyToInstance(properties, DESCRIPTION_PROPERTY, description);
        properties = archiveHelper.addStringPropertyToInstance(properties, VERSION_NUMBER_PROPERTY, versionNumber);
        properties = archiveHelper.addStringPropertyToInstance(properties, AUTHOR_PROPERTY, author);

        EntityDetail  modelEntity = archiveHelper.getEntityDetail(DESIGN_MODEL_TYPE_NAME,
                                                                  idToGUIDMap.getGUID(qualifiedName),
                                                                  properties,
                                                                  InstanceStatus.ACTIVE,
                                                                  null);

        archiveBuilder.addEntity(modelEntity);

        return modelEntity.getGUID();
    }


    /**
     * Create an element for a model group and link it to a model and any parent model group.
     *
     * @param modelId unique identifier of model (or null)
     * @param parentGroupId unique identifier of parent (or null)
     * @param qualifiedName unique name for the model
     * @param displayName display name
     * @param technicalName non-spaced name
     * @param description description
     * @param versionNumber version description
     * @param author author
     * @return guid of model group
     */
    protected String addDesignModelGroup(String   modelId,
                                         String   parentGroupId,
                                         String   qualifiedName,
                                         String   displayName,
                                         String   technicalName,
                                         String   description,
                                         String   versionNumber,
                                         String   author)
    {
        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(null, QUALIFIED_NAME_PROPERTY, qualifiedName);
        properties = archiveHelper.addStringPropertyToInstance(properties, DISPLAY_NAME_PROPERTY, displayName);
        properties = archiveHelper.addStringPropertyToInstance(properties, TECHNICAL_NAME_PROPERTY, technicalName);
        properties = archiveHelper.addStringPropertyToInstance(properties, DESCRIPTION_PROPERTY, description);
        properties = archiveHelper.addStringPropertyToInstance(properties, VERSION_NUMBER_PROPERTY, versionNumber);
        properties = archiveHelper.addStringPropertyToInstance(properties, AUTHOR_PROPERTY, author);

        EntityDetail  modelGroupEntity = archiveHelper.getEntityDetail(DESIGN_MODEL_GROUP_TYPE_NAME,
                                                                       idToGUIDMap.getGUID(qualifiedName),
                                                                       properties,
                                                                       InstanceStatus.ACTIVE,
                                                                       null);

        archiveBuilder.addEntity(modelGroupEntity);

        if (modelId != null)
        {
            EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(modelId));
            EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(modelGroupEntity.getGUID()));

            archiveBuilder.addRelationship(archiveHelper.getRelationship(DESIGN_MODEL_ELEMENT_OWNERSHIP_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(modelId + "_to_" + modelGroupEntity.getGUID()),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));

            archiveBuilder.addRelationship(archiveHelper.getRelationship(DESIGN_MODEL_GROUP_OWNERSHIP_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(modelId + "_to_" + modelGroupEntity.getGUID()),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        if (parentGroupId != null)
        {
            EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(parentGroupId));
            EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(modelGroupEntity.getGUID()));

            archiveBuilder.addRelationship(archiveHelper.getRelationship(DESIGN_MODEL_GROUP_HIERARCHY_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(parentGroupId + "_to_" + modelGroupEntity.getGUID()),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        return modelGroupEntity.getGUID();
    }


    /**
     * Create an element of a concept model.
     *
     * @param typeName unique name of the element's type
     * @param modelId unique identifier of model (or null)
     * @param groupIds list of model groups to add this element to
     * @param qualifiedName unique name for the model
     * @param displayName display name
     * @param technicalName non-spaced name
     * @param description description
     * @param versionNumber version description
     * @param author author
     * @return guid of element
     */
    protected String addConceptModelElement(String         typeName,
                                            String         modelId,
                                            List<String>   groupIds,
                                            String         qualifiedName,
                                            String         displayName,
                                            String         technicalName,
                                            String         description,
                                            String         versionNumber,
                                            String         author)
    {
        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(null, QUALIFIED_NAME_PROPERTY, qualifiedName);
        properties = archiveHelper.addStringPropertyToInstance(properties, DISPLAY_NAME_PROPERTY, displayName);
        properties = archiveHelper.addStringPropertyToInstance(properties, TECHNICAL_NAME_PROPERTY, technicalName);
        properties = archiveHelper.addStringPropertyToInstance(properties, DESCRIPTION_PROPERTY, description);
        properties = archiveHelper.addStringPropertyToInstance(properties, VERSION_NUMBER_PROPERTY, versionNumber);
        properties = archiveHelper.addStringPropertyToInstance(properties, AUTHOR_PROPERTY, author);

        EntityDetail  modelElementEntity = archiveHelper.getEntityDetail(typeName,
                                                                         idToGUIDMap.getGUID(qualifiedName),
                                                                         properties,
                                                                         InstanceStatus.ACTIVE,
                                                                         null);

        archiveBuilder.addEntity(modelElementEntity);

        if (modelId != null)
        {
            EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(modelId));
            EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(modelElementEntity.getGUID()));

            archiveBuilder.addRelationship(archiveHelper.getRelationship(DESIGN_MODEL_ELEMENT_OWNERSHIP_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(modelId + "_to_" + modelElementEntity.getGUID()),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        if (groupIds != null)
        {
            for (String  groupId : groupIds)
            {
                EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(groupId));
                EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(modelElementEntity.getGUID()));

                archiveBuilder.addRelationship(archiveHelper.getRelationship(DESIGN_MODEL_GROUP_MEMBERSHIP_TYPE_NAME,
                                                                             idToGUIDMap.getGUID(groupId + "_to_" + modelElementEntity.getGUID()),
                                                                             null,
                                                                             InstanceStatus.ACTIVE,
                                                                             end1,
                                                                             end2));
            }
        }

        return modelElementEntity.getGUID();
    }


    /**
     * Create an element that describes a concept bead.
     *
     * @param modelId unique identifier of model (or null)
     * @param groupIds list of model groups to add this element to
     * @param qualifiedName unique name for the model
     * @param displayName display name
     * @param technicalName non-spaced name
     * @param description description
     * @param versionNumber version description
     * @param author author
     * @return guid of element
     */
    protected String addConceptBead(String         modelId,
                                    List<String>   groupIds,
                                    String         qualifiedName,
                                    String         displayName,
                                    String         technicalName,
                                    String         description,
                                    String         versionNumber,
                                    String         author)
    {
        return this.addConceptModelElement(CONCEPT_BEAD_TYPE_NAME,
                                           modelId,
                                           groupIds,
                                           qualifiedName,
                                           displayName,
                                           technicalName,
                                           description,
                                           versionNumber,
                                           author);
    }


    /**
     * Create a concept bead link.
     *
     * @param modelId unique identifier of model (or null)
     * @param groupIds list of model groups to add this element to
     * @param qualifiedName unique name for the model
     * @param displayName display name
     * @param technicalName non-spaced name
     * @param description description
     * @param versionNumber version description
     * @param author author
     * @return guid of element
     */
    protected String addConceptBeadLink(String         modelId,
                                        List<String>   groupIds,
                                        String         qualifiedName,
                                        String         displayName,
                                        String         technicalName,
                                        String         description,
                                        String         versionNumber,
                                        String         author)
    {
        return this.addConceptModelElement(CONCEPT_BEAD_LINK_TYPE_NAME,
                                           modelId,
                                           groupIds,
                                           qualifiedName,
                                           displayName,
                                           technicalName,
                                           description,
                                           versionNumber,
                                           author);
    }


    /**
     * Create a concept attribute.
     *
     * @param modelId unique identifier for the model element
     * @param groupIds list of model groups to add this element to
     * @param qualifiedName unique name for the model
     * @param displayName display name
     * @param technicalName non-spaced name
     * @param description description
     * @param versionNumber version description
     * @param author author
     * @return guid of element
     */
    protected String addConceptBeadAttribute(String         modelId,
                                             List<String>   groupIds,
                                             String         qualifiedName,
                                             String         displayName,
                                             String         technicalName,
                                             String         description,
                                             String         versionNumber,
                                             String         author)
    {
        return this.addConceptModelElement(CONCEPT_BEAD_ATTRIBUTE_TYPE_NAME,
                                           modelId,
                                           groupIds,
                                           qualifiedName,
                                           displayName,
                                           technicalName,
                                           description,
                                           versionNumber,
                                           author);
    }


    protected void addAttributeToBead(String     beadId,
                                      String     attributeId,
                                      int        position,
                                      int        minCardinality,
                                      int        maxCardinality,
                                      boolean    uniqueValues,
                                      boolean    orderedValues)
    {
        InstanceProperties properties = archiveHelper.addIntPropertyToInstance(null, POSITION_PROPERTY, position);
        properties = archiveHelper.addIntPropertyToInstance(properties, MIN_CARDINALITY_PROPERTY, minCardinality);
        properties = archiveHelper.addIntPropertyToInstance(properties, MAX_CARDINALITY_PROPERTY, maxCardinality);
        properties = archiveHelper.addBooleanPropertyToInstance(properties, UNIQUE_VALUES_PROPERTY, uniqueValues);
        properties = archiveHelper.addBooleanPropertyToInstance(properties, ORDERED_VALUES_PROPERTY, orderedValues);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(beadId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(attributeId));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(CONCEPT_BEAD_ATTRIBUTE_LINK_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(beadId + "_to_" + attributeId),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    protected void addLinkToBead(String                 beadId,
                                 String                 linkId,
                                 String                 attributeName,
                                 ConceptModelDecoration decoration,
                                 int                    position,
                                 int                    minCardinality,
                                 int                    maxCardinality,
                                 boolean                uniqueValues,
                                 boolean                orderedValues)
    {
        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(null, ATTRIBUTE_NAME_PROPERTY, attributeName);
        // properties = archiveHelper.addEnumPropertyToInstance(properties, DECORATION_PROPERTY, position);
        properties = archiveHelper.addIntPropertyToInstance(properties, POSITION_PROPERTY, position);
        properties = archiveHelper.addIntPropertyToInstance(properties, MIN_CARDINALITY_PROPERTY, minCardinality);
        properties = archiveHelper.addIntPropertyToInstance(properties, MAX_CARDINALITY_PROPERTY, maxCardinality);
        properties = archiveHelper.addBooleanPropertyToInstance(properties, UNIQUE_VALUES_PROPERTY, uniqueValues);
        properties = archiveHelper.addBooleanPropertyToInstance(properties, ORDERED_VALUES_PROPERTY, orderedValues);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(beadId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(linkId));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(CONCEPT_BEAD_RELATIONSHIP_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(beadId + "_to_" + linkId),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Returns the open metadata type archive containing all of the content loaded by the subclass.
     *
     * @return populated open metadata archive object
     */
    protected OpenMetadataArchive getOpenMetadataArchive()
    {
        System.out.println("GUIDs map size: " + idToGUIDMap.getSize());

        idToGUIDMap.saveGUIDs();

        return archiveBuilder.getOpenMetadataArchive();
    }


    /**
     * Throws an exception if there is a problem building the archive.
     *
     * @param methodName calling method
     */
    protected void logBadArchiveContent(String   methodName)
    {
        /*
         * This is a logic error since it means the creation of the archive builder threw an exception
         * in the constructor and so this object should not be used.
         */
        DesignModelArchiveErrorCode errorCode    = DesignModelArchiveErrorCode.ARCHIVE_UNAVAILABLE;
        String                      errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

        throw new OMRSLogicErrorException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction());
    }
}
